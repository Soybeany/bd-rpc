package com.soybeany.rpc.consumer.exception;

import com.soybeany.rpc.core.exception.RpcPluginException;
import com.soybeany.rpc.core.model.RpcServerInfo;
import com.soybeany.sync.core.exception.SyncRequestException;
import lombok.Getter;

/**
 * @author Soybeany
 * @date 2022/2/8
 */
@Getter
public class RpcRequestException extends RpcPluginException {

    private final RpcServerInfo serverInfo;

    public RpcRequestException(RpcServerInfo serverInfo, SyncRequestException target) {
        super(target.getMessage());
        this.serverInfo = serverInfo;
    }

    @Override
    public String toString() {
        return serverInfo + ":" + getMessage();
    }

}
