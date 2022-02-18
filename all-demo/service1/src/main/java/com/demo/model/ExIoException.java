package com.demo.model;

import com.soybeany.rpc.consumer.api.IServerInfoReceiver;
import com.soybeany.rpc.core.model.RpcServerInfo;
import lombok.Getter;

import java.io.IOException;

/**
 * @author Soybeany
 * @date 2022/2/11
 */
public class ExIoException extends IOException implements IServerInfoReceiver {

    @Getter
    private RpcServerInfo serverInfo;

    public ExIoException(String message) {
        super(message);
    }

    @Override
    public void onSetupServerInfo(RpcServerInfo info) {
        serverInfo = info;
    }
}
