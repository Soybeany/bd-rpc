package com.soybeany.rpc.provider;

import com.soybeany.rpc.core.anno.BdRpc;
import com.soybeany.rpc.core.api.IServiceInvoker;
import com.soybeany.rpc.core.exception.RpcPluginException;
import com.soybeany.rpc.core.model.BaseRpcClientPlugin;
import com.soybeany.rpc.core.model.MethodInfo;
import com.soybeany.rpc.core.model.ServerInfo;
import com.soybeany.rpc.core.utl.ReflectUtils;
import com.soybeany.sync.core.model.SyncDTO;
import com.soybeany.util.file.BdFileUtils;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.soybeany.rpc.core.model.BdRpcConstants.*;
import static com.soybeany.sync.core.util.RequestUtils.GSON;

/**
 * @author Soybeany
 * @date 2021/10/27
 */
@AllArgsConstructor
public class RpcProviderPlugin extends BaseRpcClientPlugin<RpcProviderPlugin> implements IServiceInvoker {

    private final Map<String, Object> serviceMap = new HashMap<>();
    private final String authorizationToken = BdFileUtils.getUuid();
    private final ServerInfo serverInfo = new ServerInfo();

    private final String system;
    private final ApplicationContext appContext;
    private final String invokeUrl;
    private final String[] pkgToScan;

    @Override
    public void onSendSync(Map<String, String> result) {
        super.onSendSync(result);
        result.put(KEY_ACTION, ACTION_REGISTER_PROVIDERS);
        result.put(KEY_PROVIDER_INFO, GSON.toJson(serverInfo));
        result.put(KEY_SERVICE_ID_ARR, GSON.toJson(serviceMap.keySet()));
    }

    @Override
    public String onSetupSyncTagToHandle() {
        return TAG;
    }

    @SuppressWarnings("AlibabaAvoidManuallyCreateThread")
    @Override
    public RpcProviderPlugin init() {
        // 配置服务器信息
        serverInfo.setInvokeUrl(invokeUrl);
        serverInfo.setAuthorization(authorizationToken);
        // 扫描
        new Thread(() -> {
            for (String name : appContext.getBeanDefinitionNames()) {
                Object bean = appContext.getBean(name);
                for (String path : onSetupPkgPathToScan()) {
                    Optional.ofNullable(ReflectUtils.getAnnotation(path, BdRpc.class, bean.getClass()))
                            .ifPresent(bdRpc -> onHandleBean(bdRpc, bean));
                }
            }
        }).start();
        return this;
    }

    @Override
    protected String onSetupSystem() {
        return system;
    }

    @Override
    protected String[] onSetupPkgPathToScan() {
        return pkgToScan;
    }

    @Override
    public SyncDTO invoke(HttpServletRequest request, HttpServletResponse response) {
        // 凭证校验
        if (!authorizationToken.equals(request.getHeader(HEADER_AUTHORIZATION))) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }
        // 处理rpc调用
        try {
            String param = request.getParameter(KEY_METHOD_INFO);
            return SyncDTO.norm(invoke(GSON.fromJson(param, MethodInfo.class)));
        } catch (Throwable throwable) {
            return SyncDTO.error(throwable);
        }
    }

    // ***********************内部方法****************************

    private Object invoke(MethodInfo info) throws Throwable {
        Object obj = serviceMap.get(info.getServiceId());
        Method method = info.getMethod(obj);
        try {
            return method.invoke(obj, info.getArgs(method));
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }

    private void onHandleBean(BdRpc bdRpc, Object bean) {
        String id = getId(bdRpc);
        Object previous = serviceMap.put(id, bean);
        if (null != previous) {
            throw new RpcPluginException("@BdRpc的serviceId(" + id + ")需唯一");
        }
    }

}
