package com.demo;

import com.demo.model.TestParam;
import com.demo.model.TestVO;
import com.soybeany.rpc.core.anno.BdRpc;
import com.soybeany.rpc.core.anno.BdRpcBatch;
import com.soybeany.rpc.core.anno.BdRpcCache;

import java.util.List;

/**
 * @author Soybeany
 * @date 2021/10/29
 */
@BdRpc(serviceId = "test")
public interface ITestService {

    @BdRpcCache(desc = "测试", ttl = 10, useMd5Key = false)
    List<TestVO> getValue(List<TestParam> param) throws Exception;

    @BdRpcCache(desc = "测试2", ttl = 5, useMd5Key = false, storageId = "t1")
    String getValue2();

    @BdRpcCache(desc = "测试3", ttl = 5, useMd5Key = false, storageId = "t1")
    String getValue3();

    @BdRpcBatch(methodId = "batch")
    String getBatchValue(String input);

}
