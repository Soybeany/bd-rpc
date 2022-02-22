package com.demo.service2.impl;

import com.demo.service.IShareStorageTestService;
import org.springframework.stereotype.Service;

/**
 * @author Soybeany
 * @date 2022/2/22
 */
@Service
public class ShareStorageTestServiceImpl implements IShareStorageTestService {

    private static int VALUE = 0;

    @Override
    public synchronized int getValue() {
        return VALUE++;
    }

    @Override
    public synchronized int getValue2() {
        return VALUE++;
    }

}
