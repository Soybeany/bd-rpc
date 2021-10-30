package com.soybeany.rpc.demo.provider;

import org.springframework.stereotype.Component;

/**
 * @author Soybeany
 * @date 2021/10/29
 */
@Component
public class ITestServiceImpl implements ITestService {
    @Override
    public TestVO getValue(TestParam param) {
        return new TestVO(param.getB() + param.getA());
    }
}
