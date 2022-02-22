package com.demo.service;

import com.soybeany.rpc.core.anno.BdRpc;

/**
 * @author Soybeany
 * @date 2021/10/29
 */
@BdRpc
public interface ISimpleTestService {

    String getValue(String input);

}
