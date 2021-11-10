package com.soybeany.rpc.provider;

import com.soybeany.rpc.core.anno.BdRpc;
import com.soybeany.rpc.core.exception.RpcPluginException;
import com.soybeany.rpc.core.model.BaseRpcClientPlugin;
import com.soybeany.rpc.core.model.MethodInfo;
import com.soybeany.rpc.core.model.ServerInfo;
import com.soybeany.rpc.core.utl.ReflectUtils;
import com.soybeany.rpc.core.utl.ServiceInvoker;
import com.soybeany.sync.core.model.Context;
import com.soybeany.sync.core.util.NetUtils;
import com.soybeany.util.file.BdFileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.soybeany.rpc.core.model.BdRpcConstants.*;
import static com.soybeany.sync.core.util.RequestUtils.GSON;

/**
 *
 * @author Soybeany
 * @date 2021/10/27
 */
public abstract class BaseRpcProviderPlugin extends BaseRpcClientPlugin implements ServiceInvoker {

    @Autowired
    private ApplicationContext appContext;

    private final String address = NetUtils.getLocalIpAddress();
    private final Map<String, Object> serviceMap = new HashMap<>();
    private final String authorizationToken = BdFileUtils.getUuid();

    @Override
    public void onSendSync(Context ctx, Map<String, String> result) {
        result.put(KEY_ACTION, ACTION_REGISTER_PROVIDERS);
        result.put(KEY_PROVIDER_INFO, GSON.toJson(onGetServerInfo()));
        result.put(KEY_SERVICE_ID_ARR, GSON.toJson(serviceMap.keySet()));
    }

    @Override
    public void onHandleSync(Context ctx, Map<String, String> param) {

    }

    @Override
    public String onSetupSyncTagToHandle() {
        return TAG;
    }

    @Override
    public Object invoke(MethodInfo info) throws Throwable {
        Object obj = serviceMap.get(info.getServiceId());
        Method method = info.getMethod(obj);
        try {
            return method.invoke(obj, info.getArgs(method));
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }

    @PostConstruct
    private void onInit() {
        for (String name : appContext.getBeanDefinitionNames()) {
            Object bean = appContext.getBean(name);
            Optional.ofNullable(ReflectUtils.getAnnotation(onSetupPkgToScan(), BdRpc.class, bean.getClass()))
                    .ifPresent(bdRpc -> onHandleBean(bdRpc, bean));
        }
    }

    /**
     * 验证指定的凭证是否有效
     */
    public boolean isAuthorizationValid(String token) {
        return authorizationToken.equals(token);
    }

    // ***********************内部方法****************************

    private void onHandleBean(BdRpc bdRpc, Object bean) {
        String id = getId(bdRpc);
        Object previous = serviceMap.put(id, bean);
        if (null != previous) {
            throw new RpcPluginException("@BdRpc的serviceId(" + id + ")需唯一");
        }
    }

    private ServerInfo onGetServerInfo() {
        ServerInfo info = new ServerInfo();
        info.setAddress(address);
        info.setPort(onSetupServerPort());
        info.setContextPath(Optional.ofNullable(onSetupServerContextPath()).orElse(""));
        info.setAuthorization(authorizationToken);
        return info;
    }

    // ***********************子类实现****************************

    /**
     * 配置服务的端口号
     */
    protected abstract int onSetupServerPort();

    /**
     * 配置应用上下文
     */
    protected abstract String onSetupServerContextPath();

}
