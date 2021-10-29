package com.soybeany.rpc.demo.consumer;

import com.soybeany.rpc.model.BdRpc;

/**
 * @author Soybeany
 * @date 2021/10/29
 */
@BdRpc(serviceId = "test")
public interface ITestService {

    String getValue();

}
