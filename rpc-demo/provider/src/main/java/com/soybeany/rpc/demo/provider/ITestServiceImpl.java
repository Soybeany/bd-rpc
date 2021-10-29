package com.soybeany.rpc.demo.provider;

import org.springframework.stereotype.Component;

/**
 * @author Soybeany
 * @date 2021/10/29
 */
@Component
public class ITestServiceImpl implements ITestService {
    @Override
    public String getValue() {
        return "success";
    }
}
