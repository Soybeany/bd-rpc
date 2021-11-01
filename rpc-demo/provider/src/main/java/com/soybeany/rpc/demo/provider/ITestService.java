package com.soybeany.rpc.demo.provider;

import com.soybeany.rpc.core.anno.BdRpc;

import java.util.List;

/**
 * @author Soybeany
 * @date 2021/10/29
 */
@BdRpc(serviceId = "test")
public interface ITestService {

    List<TestVO> getValue(List<TestParam> param);

}
