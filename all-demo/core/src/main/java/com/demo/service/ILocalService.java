package com.demo.service;

import com.soybeany.rpc.core.anno.BdRpc;

/**
 * @author Soybeany
 * @date 2022/2/22
 */
@BdRpc
public interface ILocalService {

    String getValue();

}
