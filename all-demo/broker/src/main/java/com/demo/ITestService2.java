package com.demo;

import com.demo.model.TestParam;
import com.demo.model.TestVO;
import com.soybeany.rpc.core.anno.BdRpc;

import java.util.List;

/**
 * @author Soybeany
 * @date 2021/10/29
 */
@BdRpc(serviceId = "test")
public interface ITestService2 {

    List<TestVO> getValue(List<TestParam> param) throws Exception;

    String getValue2();

    String getValue3();

    String getBatchValue(String input);

}
