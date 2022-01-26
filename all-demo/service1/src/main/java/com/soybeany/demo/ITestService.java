package com.soybeany.demo;

import com.soybeany.demo.model.TestParam;
import com.soybeany.demo.model.TestVO;
import com.soybeany.rpc.core.anno.BdCache;
import com.soybeany.rpc.core.anno.BdRpc;

import java.util.List;

/**
 * @author Soybeany
 * @date 2021/10/29
 */
@BdRpc(serviceId = "test")
public interface ITestService {

    @BdCache(desc = "测试", expiry = 10 * 1000, useMd5Key = false)
    List<TestVO> getValue(List<TestParam> param) throws Exception;

}
