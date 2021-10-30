package com.soybeany.rpc.demo.provider;

import com.soybeany.rpc.core.model.BdRpc;

/**
 * @author Soybeany
 * @date 2021/10/29
 */
@BdRpc(serviceId = "test")
public interface ITestService {

    TestVO getValue(TestParam param);

}
