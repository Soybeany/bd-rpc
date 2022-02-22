package com.demo.service;

import com.soybeany.rpc.core.anno.BdRpc;
import com.soybeany.rpc.core.anno.BdRpcCache;

/**
 * @author Soybeany
 * @date 2022/2/22
 */
@BdRpc
public interface IShareStorageTestService {

    @BdRpcCache(ttl = 1, useMd5Key = false, storageId = "t1")
    int getValue();

    @BdRpcCache(ttl = 1, useMd5Key = false, storageId = "t1")
    int getValue2();

}
