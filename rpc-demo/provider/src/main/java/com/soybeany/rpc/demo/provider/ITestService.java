package com.soybeany.rpc.demo.provider;

import com.soybeany.rpc.core.model.BdRpc;

import java.util.List;

/**
 * @author Soybeany
 * @date 2021/10/29
 */
@BdRpc(serviceId = "test")
public interface ITestService {

    TestVO getValue(List<TestParam> param);

}
