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

    @BdRpcCache(desc = "测试", expiry = 10 * 1000, useMd5Key = false)
    List<TestVO> getValue(List<TestParam> param) throws Exception;

    @BdRpcBatch(methodId = "batch")
    String getBatchValue(String input);

}
