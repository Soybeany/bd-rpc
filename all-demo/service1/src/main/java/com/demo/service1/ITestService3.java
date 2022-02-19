package com.demo.service1;

import com.demo.service1.model.IResult;
import com.soybeany.rpc.core.anno.BdRpc;

/**
 * @author Soybeany
 * @date 2021/10/29
 */
@BdRpc(serviceId = "test3")
public interface ITestService3 {

    //    @BdRpcCache(desc = "测试2", ttl = 10, useMd5Key = false)
    IResult getValue() throws Exception;

}
