package com.demo.service;

import com.demo.model.Interface;
import com.demo.model.ResultVO;
import com.demo.model.TestParam;
import com.soybeany.rpc.core.anno.BdRpc;
import com.soybeany.rpc.core.anno.BdRpcSerialize;

import java.util.List;

/**
 * @author Soybeany
 * @date 2022/2/22
 */
@BdRpc
public interface IObjTestService {

    List<ResultVO> getValue(List<TestParam> param);

    @BdRpcSerialize
    Interface getValue(@BdRpcSerialize Interface i);

}
