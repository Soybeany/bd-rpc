package com.soybeany.rpc.core.api;

import com.soybeany.rpc.core.model.RpcBatchResult;
import com.soybeany.rpc.core.model.RpcServerInfo;

import java.util.Map;

/**
 * @author Soybeany
 * @date 2022/2/8
 */
public interface IRpcBatchInvoker<T> {

    Map<RpcServerInfo, RpcBatchResult<T>> invoke(Object... args);

}
