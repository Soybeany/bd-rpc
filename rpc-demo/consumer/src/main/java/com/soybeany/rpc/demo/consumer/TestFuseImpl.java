package com.soybeany.rpc.demo.consumer;

import com.soybeany.rpc.core.anno.BdFallback;
import com.soybeany.rpc.demo.model.TestParam;
import com.soybeany.rpc.demo.model.TestVO;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author Soybeany
 * @date 2021/11/1
 */
@BdFallback
@Component
public class TestFuseImpl implements ITestService {
    @Override
    public List<TestVO> getValue(List<TestParam> param) {
        TestVO vo = new TestVO();
        vo.setValue("熔断");
        return Collections.singletonList(vo);
    }
}
