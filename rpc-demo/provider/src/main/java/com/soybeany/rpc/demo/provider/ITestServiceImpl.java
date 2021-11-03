package com.soybeany.rpc.demo.provider;

import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * @author Soybeany
 * @date 2021/10/29
 */
@Component
public class ITestServiceImpl implements ITestService {
    @Override
    public List<TestVO> getValue(List<TestParam> params) throws Exception {
//        List<TestVO> result = new LinkedList<>();
//        for (TestParam param : params) {
//            result.add(new TestVO(param.getB() + param.getA()));
//        }
//        return result;
        throw new FileNotFoundException("测试异常");
    }
}
