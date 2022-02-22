package com.demo.service;

import com.demo.model.ExIoException;
import com.soybeany.rpc.core.anno.BdRpc;

/**
 * @author Soybeany
 * @date 2022/2/22
 */
@BdRpc
public interface IExceptionTestService {

    String getValue();

    String getValue2() throws Exception;

    String getValue3() throws ExIoException;

}
