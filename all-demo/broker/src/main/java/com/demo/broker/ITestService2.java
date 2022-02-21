package com.demo.broker;

import com.demo.broker.model.TestParam;
import com.demo.service1.model.TestVO;
import com.soybeany.rpc.core.anno.BdRpc;

import java.util.List;

/**
 * @author Soybeany
 * @date 2021/10/29
 */
@BdRpc(serviceId = "ITestService")
public interface ITestService2 {

    List<TestVO> getValue(List<TestParam> param) throws Exception;

    String getValue2();

    String getValue3();

    String getValue4();

    String getValue5();

    String getBatchValue(String input);

}
