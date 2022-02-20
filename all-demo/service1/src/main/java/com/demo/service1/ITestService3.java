package com.demo.service1;

import com.demo.service1.model.IResult;
import com.soybeany.rpc.core.anno.BdRpc;
import com.soybeany.rpc.core.anno.BdRpcCache;
import com.soybeany.rpc.core.anno.BdRpcSerialize;

/**
 * @author Soybeany
 * @date 2021/10/29
 */
@BdRpc(serviceId = "test3")
public interface ITestService3 {

    @BdRpcSerialize
    @BdRpcCache(desc = "测试2", ttl = 5, useMd5Key = false)
    IResult getValue() throws Exception;

}
