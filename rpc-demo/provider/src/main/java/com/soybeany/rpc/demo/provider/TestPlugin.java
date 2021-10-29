package com.soybeany.rpc.demo.provider;

import com.soybeany.rpc.model.BdRpc;
import com.soybeany.rpc.plugin.BaseRpcProviderPlugin;
import com.soybeany.sync.core.api.IClientPlugin;
import com.soybeany.sync.core.model.Context;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * @author Soybeany
 * @date 2021/10/28
 */
@Component
public class TestPlugin extends BaseRpcProviderPlugin {
    @Override
    public String onSetupSyncTagToHandle() {
        return "test";
    }

    @Override
    public void onSendSync(Context ctx, Map<String, String> result) {
        result.put("what", "a");
        System.out.println("准备发送心跳");
    }

    @Override
    public void onHandleSync(Context ctx, Map<String, String> param) {
        System.out.println("处理心跳回执:" + param);
    }

    @Override
    protected String onSetupScanPkg() {
        return "com.soybeany";
    }

    @Override
    protected void onHandleBean(BdRpc bdRpc, Object bean) {
        System.out.println(bean);
        System.out.println(bdRpc.serviceId());
        Obj obj = new Obj();
        ITestService service = (ITestService) Proxy.newProxyInstance(obj.getClass().getClassLoader(), new Class[]{ITestService.class}, new ProxyHandler(obj));
        System.out.println(service.getValue());
    }

    private static class Obj {
        Object print(String name) {
            System.out.println("代理" + name);
            return null;
        }
    }

    public static class ProxyHandler implements InvocationHandler {
        private final Obj object;

        public ProxyHandler(Obj object) {
            this.object = object;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {

            return object.print(method.getName());
        }
    }

}
