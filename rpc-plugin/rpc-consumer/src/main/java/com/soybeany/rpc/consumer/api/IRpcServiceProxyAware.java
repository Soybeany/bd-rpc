package com.soybeany.rpc.consumer.api;

/**
 * @author Soybeany
 * @date 2022/3/9
 */
public interface IRpcServiceProxyAware {

    void onSetupRpcServiceProxy(IRpcServiceProxy proxy);

}
