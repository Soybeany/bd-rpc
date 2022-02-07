package com.demo;

import com.demo.model.TestParam;
import com.demo.model.TestVO;
import com.soybeany.rpc.core.anno.BdRpcFallback;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author Soybeany
 * @date 2021/11/1
 */
@BdRpcFallback
@Component
public class TestFuseImpl implements ITestService {
    @Override
    public List<TestVO> getValue(List<TestParam> param) {
        TestVO vo = new TestVO();
        vo.setValue("熔断");
        return Collections.singletonList(vo);
    }
}
