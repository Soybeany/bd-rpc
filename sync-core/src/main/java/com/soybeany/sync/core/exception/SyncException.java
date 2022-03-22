package com.soybeany.sync.core.exception;

/**
 * @author Soybeany
 * @date 2021/12/16
 */
public class SyncException extends Exception implements ISyncExceptionMsgProvider {
    public SyncException(String message) {
        super(message);
    }

    @Override
    public String getMsg() {
        return getMessage();
    }

}
