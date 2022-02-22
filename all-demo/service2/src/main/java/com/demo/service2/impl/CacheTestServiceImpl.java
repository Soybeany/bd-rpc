package com.demo.service2.impl;

import com.demo.service.ICacheTestService;
import org.springframework.stereotype.Service;

/**
 * @author Soybeany
 * @date 2022/2/22
 */
@Service
public class CacheTestServiceImpl implements ICacheTestService {

    private static int VALUE = 0;

    @Override
    public synchronized int getValue() {
        return VALUE++;
    }

}
