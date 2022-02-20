package com.demo.service2;

import com.demo.service1.model.IResult;
import com.soybeany.rpc.core.anno.BdRpc;

/**
 * @author Soybeany
 * @date 2021/10/29
 */
@BdRpc(serviceId = "test3")
public interface ITestService3 {

    IResult getValue() throws Exception;

}
