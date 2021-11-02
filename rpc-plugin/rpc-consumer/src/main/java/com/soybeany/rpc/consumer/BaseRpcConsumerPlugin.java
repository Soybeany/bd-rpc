package com.soybeany.rpc.consumer;

import com.google.gson.reflect.TypeToken;
import com.soybeany.rpc.core.anno.BdRpc;
import com.soybeany.rpc.core.exception.RpcPluginException;
import com.soybeany.rpc.core.model.BaseRpcClientPlugin;
import com.soybeany.rpc.core.model.MethodInfo;
import com.soybeany.rpc.core.model.ServerInfo;
import com.soybeany.rpc.core.model.ServerInfoProvider;
import com.soybeany.rpc.core.utl.ServiceProvider;
import com.soybeany.sync.core.model.Context;
import com.soybeany.sync.core.util.RequestUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.*;

import static com.soybeany.rpc.core.model.BdRpcConstants.*;
import static com.soybeany.sync.core.util.RequestUtils.GSON;

/**
 * @author Soybeany
 * @date 2021/10/27
 */
public abstract class BaseRpcConsumerPlugin extends BaseRpcClientPlugin implements ServiceProvider {

    private static final String RESOURCE_PATTERN = "/**/*.class";

    // todo 首次拉全量，后续拉增量

    private static final Type PROVIDERS_TYPE = new TypeToken<Map<String, ServerInfoProvider>>() {
    }.getType();

    @Autowired
    private ApplicationContext appContext;

    /**
     * 已加载的代理（本地/远程）
     */
    private final Map<Class<?>, Object> proxies = new HashMap<>();

    /**
     * 用于熔断的实现
     */
    private final Map<Class<?>, Object> fallbackImpls = new HashMap<>();

    private final Set<String> serviceIdSet = new HashSet<>();

    /**
     * 服务器信息提供者的映射
     */
    private Map<String, ServerInfoProvider> providers;

    @Override
    public void onSendSync(Context ctx, Map<String, String> result) {
        result.put(KEY_ACTION, ACTION_GET_PROVIDERS);
        result.put(KEY_SERVICE_ID_ARR, GSON.toJson(serviceIdSet));
    }

    @Override
    public synchronized void onHandleSync(Context ctx, Map<String, String> param) {
        providers = GSON.fromJson(param.get(KEY_PROVIDER_MAP), PROVIDERS_TYPE);
    }

    @Override
    public String onSetupSyncTagToHandle() {
        return TAG;
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
        throw new RpcPluginException("没有找到指定类的实现");
    }

    // ***********************内部方法****************************

    @PostConstruct
    private void onInit() {
        scanAndSetupNeededService();
    }

    private <T> T request(ServerInfoProvider provider, MethodInfo methodInfo, Type resultType) {
        ServerInfo serverInfo = provider.get();
        String url = serverInfo.getProtocol() + "://" + serverInfo.getAddress() + ":" + serverInfo.getPort()
                + serverInfo.getContext() + PATH
                + (null != serverInfo.getSuffix() ? serverInfo.getSuffix() : "");
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", serverInfo.getAuthorization());
        Map<String, String> params = new HashMap<>();
        params.put(KEY_METHOD_INFO, GSON.toJson(methodInfo));
        try {
            return RequestUtils.request(url, headers, params, resultType);
        } catch (IOException e) {
            throw new RpcPluginException(e.getMessage());
        }
    }

    private void scanAndSetupNeededService() {
        //spring工具类，可以获取指定路径下的全部类
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        try {
            String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath(onSetupScanPkg()) + RESOURCE_PATTERN;
            Resource[] resources = resourcePatternResolver.getResources(pattern);
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
                        .ifPresent(bdRpc -> setupServiceImpl(clazz, getId(bdRpc)));
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("路径元信息解析异常:" + e.getMessage());
        }
    }

    private void setupServiceImpl(Class<?> interfaceClass, String serviceId) {
        // 记录serviceId
        serviceIdSet.add(serviceId);
        // 本地服务
        Optional.ofNullable(getBeanFromContext(interfaceClass)).ifPresent(impl -> {
            if (isFallbackImpl(impl)) {
                fallbackImpls.put(interfaceClass, impl);
            } else {
                proxies.put(interfaceClass, impl);
            }
        });
        // 远端服务
        Object instance = Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, (proxy, method, args) -> {
            ServerInfoProvider provider;
            if (null == providers || null == (provider = providers.get(serviceId))) {
                throw new RpcPluginException("暂无此id的服务提供者信息");
            }
            MethodInfo info = new MethodInfo(serviceId, method, args);
            return request(provider, info, method.getGenericReturnType());
        });
        proxies.put(interfaceClass, instance);
    }

    @SuppressWarnings("unchecked")
    private <T> T getBeanFromContext(Class<?> interfaceClass) {
        try {
            return (T) appContext.getBean(interfaceClass);
        } catch (NoSuchBeanDefinitionException ignore) {
            return null;
        }
    }
}
