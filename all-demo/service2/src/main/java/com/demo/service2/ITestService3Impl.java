package com.demo.service2;

import com.demo.service1.model.IResult;
import com.demo.service1.model.ResultImpl;
import org.springframework.stereotype.Service;

/**
 * @author Soybeany
 * @date 2022/2/19
 */
@Service
public class ITestService3Impl implements ITestService3 {
    @Override
    public IResult getValue() {
        return new ResultImpl();
    }
}
