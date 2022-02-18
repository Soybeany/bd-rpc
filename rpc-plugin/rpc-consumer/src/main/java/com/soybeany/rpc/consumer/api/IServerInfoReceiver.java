package com.soybeany.rpc.consumer.api;

import com.soybeany.rpc.core.model.RpcServerInfo;

/**
 * 用于rpc方法所抛出的异常，能接收到“抛出异常的服务器”的信息
 *
 * @author Soybeany
 * @date 2022/2/11
 */
public interface IServerInfoReceiver {

    void onSetupServerInfo(RpcServerInfo info);

}
