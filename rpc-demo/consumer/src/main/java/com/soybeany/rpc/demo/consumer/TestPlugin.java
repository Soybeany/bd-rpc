package com.soybeany.rpc.demo.consumer;

import com.soybeany.rpc.model.BdRpc;
import com.soybeany.rpc.plugin.BaseRpcConsumerPlugin;
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
public class TestPlugin extends BaseRpcConsumerPlugin {

    @Override
    public String onSetupSyncTagToHandle() {
        return "test2";
    }

    @Override
    public void onSendSync(Context ctx, Map<String, String> result) {
        ctx.getHeaders().put("hKey", "b");
        result.put("good", "b");
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

    }

}
