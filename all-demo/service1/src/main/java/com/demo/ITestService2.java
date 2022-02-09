package com.demo;

import com.soybeany.rpc.core.anno.BdRpc;
import com.soybeany.rpc.core.anno.BdRpcCache;

/**
 * @author Soybeany
 * @date 2021/10/29
 */
@BdRpc(serviceId = "test2")
public interface ITestService2 {

    @BdRpcCache(desc = "测试2", pTtl = 10 * 1000, useMd5Key = false)
    String getValue(String input) throws Exception;

}
