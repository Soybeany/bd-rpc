package com.demo.service1;

import com.soybeany.rpc.core.anno.BdRpc;
import com.soybeany.rpc.core.anno.BdRpcCache;

/**
 * @author Soybeany
 * @date 2021/10/29
 */
@BdRpc
public interface ITestService2 {

    @BdRpcCache(desc = "测试2", ttl = 10, useMd5Key = false)
    String getValue(String input) throws Exception;

}
