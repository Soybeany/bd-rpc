package com.soybeany.rpc.demo.provider;

import com.soybeany.rpc.model.BdRpc;
import com.soybeany.rpc.plugin.BaseRpcProviderPlugin;
import com.soybeany.rpc.utl.ReflectUtils;
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
    protected String onSetupScanPkg() {
        return "com.soybeany";
    }

}
