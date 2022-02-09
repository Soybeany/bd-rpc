package com.demo;

import org.springframework.stereotype.Component;

/**
 * @author Soybeany
 * @date 2021/11/1
 */
@Component
public class Test2Impl implements ITestService2 {

    @Override
    public String getValue(String input) {
        return "本地:" + input;
    }
}
