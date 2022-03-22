package com.soybeany.rpc.core.exception;

import com.soybeany.sync.core.exception.ISyncExceptionMsgProvider;

/**
 * @author Soybeany
 * @date 2021/11/1
 */
public class RpcPluginException extends RuntimeException implements ISyncExceptionMsgProvider {

    public RpcPluginException(String msg) {
        super(msg);
    }

    @Override
    public String getMsg() {
        return getMessage();
    }
}
