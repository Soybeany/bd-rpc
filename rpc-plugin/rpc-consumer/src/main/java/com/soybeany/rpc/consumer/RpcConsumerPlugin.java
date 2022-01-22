package com.soybeany.rpc.consumer;

import com.soybeany.rpc.core.anno.BdFallback;
import com.soybeany.rpc.core.anno.BdRpc;
import com.soybeany.rpc.core.api.IRpcServiceProxy;
import com.soybeany.rpc.core.client.BaseRpcClientPlugin;
import com.soybeany.rpc.core.exception.RpcPluginException;
import com.soybeany.rpc.core.exception.RpcPluginNoFallbackException;
import com.soybeany.rpc.core.model.*;
import com.soybeany.sync.core.exception.SyncRequestException;
import com.soybeany.sync.core.model.SyncClientInfo;
import com.soybeany.sync.core.model.SyncDTO;
import com.soybeany.sync.core.picker.DataPicker;
import com.soybeany.sync.core.util.RequestUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.function.Function;

import static com.soybeany.rpc.core.model.BdRpcConstants.*;
import static com.soybeany.sync.core.util.RequestUtils.GSON;

/**
 * @author Soybeany
 * @date 2021/10/27
 */
@Slf4j
@RequiredArgsConstructor
public class RpcConsumerPlugin extends BaseRpcClientPlugin<RpcConsumerInput, RpcConsumerOutput> implements IRpcServiceProxy {

    private static final String RESOURCE_PATTERN = "/**/*.class";
    private static final String TAG_SEPARATOR = ":";

    /**
     * 已加载的代理（本地/远程）
     */
    private final Map<Class<?>, Object> proxies = new HashMap<>();

    /**
     * 服务器信息提供者的映射
     */
    private final Map<String, DataPicker<ServerInfo>> pickers = new HashMap<>();

    private final Set<String> serviceIdSet = new HashSet<>();

    private final String system;
    private final String version;
    private final ApplicationContext appContext;
    private final Function<String, DataPicker<ServerInfo>> dataPickerProvider;
    private final Function<String, Integer> timeoutInSecProvider;
    private final String[] pkgToScan;

    private String md5;

    @Override
    public String onSetupSyncTagToHandle() {
        return TAG_C;
    }

    @Override
    public Class<RpcConsumerInput> onGetInputClass() {
        return RpcConsumerInput.class;
    }

    @Override
    public Class<RpcConsumerOutput> onGetOutputClass() {
        return RpcConsumerOutput.class;
    }

    @Override
    public void onStartup(SyncClientInfo info) {
        super.onStartup(info);
        //spring工具类，可以获取指定路径下的全部类
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        try {
            List<Resource> resources = new ArrayList<>();
            for (String path : onSetupPkgPathToScan()) {
                String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath(path) + RESOURCE_PATTERN;
                Resource[] partResources = resourcePatternResolver.getResources(pattern);
                Collections.addAll(resources, partResources);
            }
            //MetadataReader 的工厂类
            MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
            for (Resource resource : resources) {
                //用于读取类信息
                MetadataReader reader = readerFactory.getMetadataReader(resource);
                //扫描到的class
                String classname = reader.getClassMetadata().getClassName();
                Class<?> clazz = Class.forName(classname);
                //处理指定的注解
                Optional.ofNullable(clazz.getAnnotation(BdRpc.class))
                        .filter(bdRpc -> clazz.isInterface())
                        .ifPresent(bdRpc -> setupServiceImpl(clazz, getId(version, bdRpc), bdRpc.timeoutInSec()));
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RpcPluginException("路径元信息解析异常:" + e.getMessage());
        }
    }

    @Override
    public synchronized boolean onBeforeSync(String uid, RpcConsumerOutput output) throws Exception {
        output.setSystem(system);
        output.setServiceIds(serviceIdSet);
        output.setMd5(md5);
        return super.onBeforeSync(uid, output);
    }

    @Override
    public synchronized void onAfterSync(String uid, RpcConsumerInput input) throws Exception {
        super.onAfterSync(uid, input);
        if (!input.isUpdated()) {
            return;
        }
        md5 = input.getMd5();
        Set<String> keys = new HashSet<>(pickers.keySet());
        Optional.ofNullable(input.getProviderMap()).ifPresent(map -> {
            // 数据预处理，添加带标签的记录
            Map<String, Set<ServerInfo>> tmpMap = new HashMap<>();
            map.forEach((id, v) -> v.forEach(info -> {
                String newId = getMergedServiceId(info.getTag(), id);
                if (!newId.equals(id)) {
                    tmpMap.computeIfAbsent(newId, s -> new HashSet<>()).add(info);
                }
            }));
            map.putAll(tmpMap);
            // 更新数据
            map.forEach((id, v) -> {
                keys.remove(id);
                DataPicker<ServerInfo> picker = pickers.computeIfAbsent(id, dataPickerProvider);
                picker.set(v.toArray(new ServerInfo[0]));
            });
        });
        // 移除已失效条目
        keys.forEach(pickers::remove);
    }

    @SuppressWarnings("unchecked")
    @Override
    public synchronized <T> T get(Class<T> interfaceClass) {
        // 从代理实现中找
        T instance = (T) proxies.get(interfaceClass);
        if (null != instance) {
            return instance;
        }
        // 从spring容器中找
        T impl = getBeanFromContext(interfaceClass);
        if (null != impl) {
            return impl;
        }
        // 没有找到实现类
        throw new RpcPluginException("没有找到指定类(" + interfaceClass + ")的实现");
    }

    @Override
    public <T> ProxySelector<T> getSelector(Class<T> interfaceClass) throws RpcPluginException {
        return new ProxySelector<>(get(interfaceClass));
    }

    @Override
    protected String[] onSetupPkgPathToScan() {
        return pkgToScan;
    }

    // ***********************内部方法****************************

    private <T> T invoke(Method method, Object[] args, Object fallbackImpl, String serviceId, int timeoutInSec) throws Throwable {
        DataPicker<ServerInfo> picker;
        serviceId = getMergedServiceId(ProxySelector.getAndRemoveTag(), serviceId);
        if (null == (picker = pickers.get(serviceId))) {
            return invokeMethodOfFallbackImpl(method, args, fallbackImpl, "暂无serviceId(" + serviceId + ")的服务提供者信息");
        }
        RequestUtils.Config config = new RequestUtils.Config();
        config.getParams().put(KEY_METHOD_INFO, GSON.toJson(new MethodInfo(getSplitServiceId(serviceId), method, args)));
        config.setTimeoutInSec(timeoutInSec);
        SyncDTO dto;
        try {
            dto = RequestUtils.request(picker, serverInfo -> {
                config.getHeaders().put(HEADER_AUTHORIZATION, serverInfo.getAuthorization());
                return serverInfo.getInvokeUrl();
            }, config, SyncDTO.class, "暂无serviceId(" + serviceId + ")可用的服务提供者");
        } catch (SyncRequestException e) {
            return invokeMethodOfFallbackImpl(method, args, fallbackImpl, e.getMessage());
        }
        return dto.getIsNorm() ? dto.getData(method.getGenericReturnType()) : dto.throwException();
    }

    @SuppressWarnings("unchecked")
    private <T> T invokeMethodOfFallbackImpl(Method method, Object[] args, Object fallbackImpl, String errMsg) throws Throwable {
        log.warn(errMsg);
        if (null == fallbackImpl) {
            throw new RpcPluginNoFallbackException(errMsg);
        } else {
            return (T) method.invoke(fallbackImpl, args);
        }
    }

    private void setupServiceImpl(Class<?> interfaceClass, String serviceId, int referTimeoutInSec) {
        // 记录serviceId
        boolean success = serviceIdSet.add(serviceId);
        if (!success) {
            throw new RpcPluginException("@BdRpc的serviceId(" + serviceId + ")需唯一");
        }
        // 本地服务
        Object[] fallbackImpl = new Object[1];
        Optional.ofNullable(getBeanFromContext(interfaceClass)).ifPresent(impl -> {
            if (isFallbackImpl(impl)) {
                fallbackImpl[0] = impl;
            } else {
                proxies.put(interfaceClass, impl);
            }
        });
        // 远端服务
        int timeoutInSec = (referTimeoutInSec >= 0 ? referTimeoutInSec : timeoutInSecProvider.apply(serviceId));
        Object instance = Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class[]{interfaceClass},
                (proxy, method, args) -> invoke(method, args, fallbackImpl[0], serviceId, timeoutInSec)
        );
        proxies.put(interfaceClass, instance);
    }

    private boolean isFallbackImpl(Object obj) {
        return null != obj.getClass().getAnnotation(BdFallback.class);
    }

    @SuppressWarnings("unchecked")
    private <T> T getBeanFromContext(Class<?> interfaceClass) {
        try {
            return (T) appContext.getBean(interfaceClass);
        } catch (NoSuchBeanDefinitionException ignore) {
            return null;
        }
    }

    private String getSplitServiceId(String serviceId) {
        String[] parts = serviceId.split(TAG_SEPARATOR);
        return parts.length > 1 ? parts[1] : serviceId;
    }

    private String getMergedServiceId(String tag, String serviceId) {
        return StringUtils.hasText(tag) ? (tag + TAG_SEPARATOR + serviceId) : serviceId;
    }

}
