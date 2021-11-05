package com.soybeany.rpc.demo.provider;

import com.soybeany.rpc.demo.model.TestParam;
import com.soybeany.rpc.demo.model.TestVO;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Soybeany
 * @date 2021/10/29
 */
@Component
public class ITestServiceImpl implements ITestService {
    @Override
    public List<TestVO> getValue(List<TestParam> params) {
        List<TestVO> result = new LinkedList<>();
        for (TestParam param : params) {
            result.add(new TestVO(param.getB() + param.getA()));
        }
        return result;
    }
}
