package com.soybeany.rpc.demo.provider;

import com.soybeany.rpc.core.anno.BdRpc;
import com.soybeany.rpc.demo.model.TestParam;
import com.soybeany.rpc.demo.model.TestVO;

import java.util.List;

/**
 * @author Soybeany
 * @date 2021/10/29
 */
@BdRpc(serviceId = "test")
public interface ITestService {

    List<TestVO> getValue(List<TestParam> param) throws Exception;

}
