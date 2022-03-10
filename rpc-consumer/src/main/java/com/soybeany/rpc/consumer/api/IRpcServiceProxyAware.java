package com.soybeany.rpc.consumer.api;

import com.soybeany.sync.client.api.IClientPlugin;

import java.util.List;

/**
 * @author Soybeany
 * @date 2022/3/9
 */
public interface IRpcServiceProxyAware {

    static void invokeRpcServiceProxyAware(List<IClientPlugin<?, ?>> plugins, IRpcServiceProxy proxy) {
        for (IClientPlugin<?, ?> plugin : plugins) {
            if (plugin instanceof IRpcServiceProxyAware) {
                ((IRpcServiceProxyAware) plugin).onSetupRpcServiceProxy(proxy);
            }
        }
    }

    void onSetupRpcServiceProxy(IRpcServiceProxy proxy);

}
