package com.soybeany.demo;

import com.soybeany.rpc.core.anno.BdRpc;
import com.soybeany.demo.model.TestParam;
import com.soybeany.demo.model.TestVO;

import java.util.List;

/**
 * @author Soybeany
 * @date 2021/10/29
 */
@BdRpc(serviceId = "test")
public interface ITestService {

    List<TestVO> getValue(List<TestParam> param) throws Exception;

}
