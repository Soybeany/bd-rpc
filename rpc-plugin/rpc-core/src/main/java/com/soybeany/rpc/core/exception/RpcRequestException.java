package com.soybeany.rpc.core.exception;

import com.soybeany.rpc.core.model.RpcServerInfo;
import lombok.Getter;

/**
 * @author Soybeany
 * @date 2022/2/8
 */
@Getter
public class RpcRequestException extends RpcPluginException {

    private final RpcServerInfo serverInfo;
    private final Exception targetException;

    public RpcRequestException(RpcServerInfo serverInfo, Exception target) {
        super(target.getMessage());
        this.serverInfo = serverInfo;
        this.targetException = target;
    }

    @Override
    public String toString() {
        return serverInfo + ":" + targetException.getMessage();
    }
}
