package com.soybeany.rpc.provider;

import com.soybeany.rpc.core.model.BaseRpcClientPlugin;
import com.soybeany.rpc.core.anno.BdRpc;
import com.soybeany.rpc.core.model.MethodInfo;
import com.soybeany.rpc.core.model.ServerInfo;
import com.soybeany.rpc.core.utl.ReflectUtils;
import com.soybeany.rpc.core.utl.ServiceInvoker;
import com.soybeany.sync.core.model.Context;
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
 * // todo serverInfoProvider增加自动剔除失败次数较多的server
 * // todo 支持熔断实现的解析、概率半熔断
 * // todo 增加可配置项
 * // todo 支持本地服务与远端代理服务
 * // todo 增加异常类
 *
 * @author Soybeany
 * @date 2021/10/27
 */
public abstract class BaseRpcProviderPlugin extends BaseRpcClientPlugin implements ServiceInvoker {

    @Autowired
    private ApplicationContext appContext;

    private final Map<String, Object> serviceMap = new HashMap<>();

    @Override
    public void onSendSync(Context ctx, Map<String, String> result) {
        ServerInfo info = new ServerInfo();
        info.setAddress("localhost");
        info.setPort(8081);
        info.setContext("");
        info.setAuthorization("123456");

        result.put(KEY_ACTION, ACTION_REGISTER_PROVIDERS);
        result.put(KEY_PROVIDER_INFO, GSON.toJson(info));
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
    public Object invoke(MethodInfo info) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object obj = serviceMap.get(info.getServiceId());
        Method method = info.getMethod(obj);
        return method.invoke(obj, info.getArgs(method));
    }

    @PostConstruct
    private void onInit() {
        for (String name : appContext.getBeanDefinitionNames()) {
            Object bean = appContext.getBean(name);
            Optional.ofNullable(ReflectUtils.getAnnotation(onSetupScanPkg(), BdRpc.class, bean.getClass()))
                    .ifPresent(bdRpc -> onHandleBean(bdRpc, bean));
        }
    }

    // ***********************内部方法****************************

    private void onHandleBean(BdRpc bdRpc, Object bean) {
        // todo 去重
        serviceMap.put(getId(bdRpc), bean);
    }


}
