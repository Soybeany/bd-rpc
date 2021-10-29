package com.soybeany.rpc.plugin;

import com.google.gson.reflect.TypeToken;
import com.soybeany.rpc.model.BdRpc;
import com.soybeany.rpc.model.MethodInfo;
import com.soybeany.rpc.model.ServerInfo;
import com.soybeany.rpc.model.ServerInfoProvider;
import com.soybeany.rpc.utl.ReflectUtils;
import com.soybeany.rpc.utl.ServiceProvider;
import com.soybeany.sync.core.model.Context;
import com.soybeany.sync.core.model.SyncSender;
import com.soybeany.sync.core.util.RequestUtils;

import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.soybeany.sync.core.util.RequestUtils.GSON;

/**
 * @author Soybeany
 * @date 2021/10/27
 */
public abstract class BaseRpcConsumerPlugin extends BaseRpcClientPlugin implements ServiceProvider {

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

    private SyncSender sender;

    @Override
    public void onStartup(SyncSender sender) {
        this.sender = sender;
    }

    @Override
    public void onSendSync(Context ctx, Map<String, String> result) {
        result.put(Constants.KEY_ACTION, Constants.ACTION_GET_PROVIDERS);
        result.put(Constants.KEY_SERVICE_ID_ARR, GSON.toJson(serviceIdSet));
    }

    @Override
    public synchronized void onHandleSync(Context ctx, Map<String, String> param) {
        providers = GSON.fromJson(param.get(Constants.KEY_PROVIDER_MAP), PROVIDERS_TYPE);
    }

    @Override
    public String onSetupSyncTagToHandle() {
        return Constants.TAG;
    }

    @Override
    protected void onHandleBean(BdRpc bdRpc, Object bean) {
        serviceIdSet.add(getId(bdRpc));
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
            serviceIdSet.add(serviceId);
            sender.send(null);
            if (null == providers || null == (provider = providers.get(serviceId))) {
                throw new RuntimeException("暂无此id的服务提供者信息");
            }
            instance = (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, (proxy, method, args) -> {
                MethodInfo info = new MethodInfo(serviceId, method.getName(), toClassNames(method.getParameterTypes()), args);
                return request(provider, info, method.getReturnType());
            });
            proxies.put(interfaceClass, instance);
        }
        return instance;
    }

    // ***********************内部方法****************************

    private <T> T request(ServerInfoProvider provider, MethodInfo methodInfo, Class<T> resultClass) {
        ServerInfo serverInfo = provider.get();
        String url = "http://" + serverInfo.getAddress() + ":" + serverInfo.getPort()
                + serverInfo.getContext() + Constants.PATH
                + (null != serverInfo.getSuffix() ? serverInfo.getSuffix() : "");
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", serverInfo.getAuthorization());
        Map<String, String> params = new HashMap<>();
        params.put(Constants.KEY_METHOD_INFO, GSON.toJson(methodInfo));
        return RequestUtils.request(url, headers, params, resultClass);
    }

    private String[] toClassNames(Class<?>[] classes) {
        String[] result = new String[classes.length];
        for (int i = 0; i < classes.length; i++) {
            result[i] = classes[i].getName();
        }
        return result;
    }

}
