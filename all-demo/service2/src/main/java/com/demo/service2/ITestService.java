package com.demo.service2;

import com.demo.service1.model.ExIoException;
import com.demo.service1.model.TestVO;
import com.demo.service2.model.TestParam;
import com.soybeany.rpc.core.anno.BdRpc;
import com.soybeany.rpc.core.anno.BdRpcBatch;

import java.io.IOException;
import java.util.List;

/**
 * @author Soybeany
 * @date 2021/10/29
 */
@BdRpc
public interface ITestService {

    List<TestVO> getValue(List<TestParam> param) throws Exception;

    String getValue2() throws ExIoException;

    String getValue3() throws IOException;

    String getValue4();

    String getValue5();

    @BdRpcBatch(methodId = "batch")
    String getBatchValue(String input);

}
