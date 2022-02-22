package com.demo.service2.impl;

import com.demo.service.IFallbackTestService;
import org.springframework.stereotype.Service;

/**
 * @author Soybeany
 * @date 2022/2/22
 */
@Service
public class FallbackTestServiceImpl implements IFallbackTestService {
    @Override
    public String getValue() {
        return "来自service2";
    }
}
