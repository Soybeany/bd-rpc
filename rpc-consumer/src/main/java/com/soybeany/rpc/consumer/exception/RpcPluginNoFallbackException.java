package com.soybeany.rpc.consumer.exception;

import com.soybeany.rpc.core.exception.RpcPluginException;

/**
 * @author Soybeany
 * @date 2021/12/21
 */
public class RpcPluginNoFallbackException extends RpcPluginException {
    public RpcPluginNoFallbackException(String msg) {
        super(msg);
    }
}
