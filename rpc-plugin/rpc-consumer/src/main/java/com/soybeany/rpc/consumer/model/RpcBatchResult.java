package com.soybeany.rpc.consumer.model;

import lombok.Data;

/**
 * @author Soybeany
 * @date 2022/2/7
 */
@Data
public class RpcBatchResult<T> {

    private final Boolean isNorm;
    private final T data;
    private final Throwable exception;

    public static <T> RpcBatchResult<T> norm(T data) {
        return new RpcBatchResult<>(true, data, null);
    }

    public static <T> RpcBatchResult<T> error(Throwable e) {
        return new RpcBatchResult<>(false, null, e);
    }

}
