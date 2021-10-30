package com.soybeany.rpc.consumer;

import com.google.gson.reflect.TypeToken;
import com.soybeany.rpc.core.model.*;
import com.soybeany.rpc.core.utl.ReflectUtils;
import com.soybeany.rpc.core.utl.ServiceProvider;
import com.soybeany.sync.core.model.Context;
import com.soybeany.sync.core.util.RequestUtils;
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

    /**
     * 已加载的代理
     */
    private final Map<Class<?>, Object> proxies = new HashMap<>();

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
        T instance = (T) proxies.get(interfaceClass);
        if (null == instance) {
            BdRpc bdRpc = ReflectUtils.getAnnotation(onSetupScanPkg(), BdRpc.class, interfaceClass);
            if (null == bdRpc) {
                throw new RuntimeException("指定的接口需要添加@BdRpc注解");
            }
            ServerInfoProvider provider;
            String serviceId = getId(bdRpc);
            if (null == providers || null == (provider = providers.get(serviceId))) {
                throw new RuntimeException("暂无此id的服务提供者信息");
            }
            instance = (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, (proxy, method, args) -> {
                MethodInfo info = new MethodInfo(serviceId, method, args);
                return request(provider, info, method.getReturnType());
            });
            proxies.put(interfaceClass, instance);
        }
        return instance;
    }

    // ***********************内部方法****************************

    @PostConstruct
    private void onInit() {
        scanNeededServiceIds();
    }

    private <T> T request(ServerInfoProvider provider, MethodInfo methodInfo, Class<T> resultClass) {
        ServerInfo serverInfo = provider.get();
        String url = "http://" + serverInfo.getAddress() + ":" + serverInfo.getPort()
                + serverInfo.getContext() + PATH
                + (null != serverInfo.getSuffix() ? serverInfo.getSuffix() : "");
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", serverInfo.getAuthorization());
        Map<String, String> params = new HashMap<>();
        params.put(KEY_METHOD_INFO, GSON.toJson(methodInfo));
        return RequestUtils.request(url, headers, params, resultClass);
    }

    private void scanNeededServiceIds() {
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
                        .ifPresent(bdRpc -> serviceIdSet.add(getId(bdRpc)));
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("路径元信息解析异常:" + e.getMessage());
        }
    }

}
