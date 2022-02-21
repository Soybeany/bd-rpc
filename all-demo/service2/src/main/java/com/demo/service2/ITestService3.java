package com.demo.service2;

import com.demo.service1.model.IResult;
import com.soybeany.rpc.core.anno.BdRpc;

/**
 * @author Soybeany
 * @date 2021/10/29
 */
@BdRpc
public interface ITestService3 {

    IResult getValue(IResult r) throws Exception;

}
