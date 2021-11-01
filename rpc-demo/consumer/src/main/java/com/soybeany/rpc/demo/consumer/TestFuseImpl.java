package com.soybeany.rpc.demo.consumer;

import com.soybeany.rpc.core.anno.BdFuse;
import com.soybeany.rpc.demo.provider.TestParam;
import com.soybeany.rpc.demo.provider.TestVO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Soybeany
 * @date 2021/11/1
 */
@BdFuse
@Component
public class TestFuseImpl implements ITestService {
    @Override
    public List<TestVO> getValue(List<TestParam> param) {
        return null;
    }
}
