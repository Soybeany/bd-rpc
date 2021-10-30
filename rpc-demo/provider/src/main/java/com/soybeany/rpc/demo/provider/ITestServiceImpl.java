package com.soybeany.rpc.demo.provider;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Soybeany
 * @date 2021/10/29
 */
@Component
public class ITestServiceImpl implements ITestService {
    @Override
    public TestVO getValue(List<TestParam> params) {
        TestParam param = params.get(0);
        return new TestVO(param.getB() + param.getA());
    }
}
