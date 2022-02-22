package com.demo.service;

import com.soybeany.rpc.core.anno.BdRpc;
import com.soybeany.rpc.core.anno.BdRpcBatch;

/**
 * @author Soybeany
 * @date 2022/2/22
 */
@BdRpc
public interface IBatchTestService {

    @BdRpcBatch(methodId = "batch")
    String getBatchValue(String input);

}
