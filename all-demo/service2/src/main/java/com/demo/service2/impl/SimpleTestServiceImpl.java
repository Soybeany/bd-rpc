package com.demo.service2.impl;

import com.demo.service.ISimpleTestService;
import org.springframework.stereotype.Service;

/**
 * @author Soybeany
 * @date 2022/2/22
 */
@Service
public class SimpleTestServiceImpl implements ISimpleTestService {
    @Override
    public String getValue(String input) {
        return "来自service2";
    }
}
