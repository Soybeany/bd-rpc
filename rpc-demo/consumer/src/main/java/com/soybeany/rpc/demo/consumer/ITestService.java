package com.soybeany.rpc.demo.consumer;

import com.soybeany.rpc.demo.provider.TestParam;
import com.soybeany.rpc.demo.provider.TestVO;
import com.soybeany.rpc.model.BdRpc;

/**
 * @author Soybeany
 * @date 2021/10/29
 */
@BdRpc(serviceId = "test")
public interface ITestService {

    TestVO getValue(TestParam param);

}
