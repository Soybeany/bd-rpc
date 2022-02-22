package com.demo.service;

import com.soybeany.rpc.core.anno.BdRpc;
import com.soybeany.rpc.core.anno.BdRpcCache;

/**
 * @author Soybeany
 * @date 2022/2/22
 */
@BdRpc
public interface ICacheTestService {

    @BdRpcCache(ttl = 2, useMd5Key = false)
    int getValue();

}
