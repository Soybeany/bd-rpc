package com.demo.service1;

import com.soybeany.rpc.client.anno.BdRpc;
import com.soybeany.rpc.consumer.anno.BdRpcCache;

/**
 * @author Soybeany
 * @date 2021/10/29
 */
@BdRpc(serviceId = "test2")
public interface ITestService2 {

    @BdRpcCache(desc = "测试2", ttl = 10, useMd5Key = false)
    String getValue(String input) throws Exception;

}
