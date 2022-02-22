package com.demo.broker.impl;

import com.demo.service.IBatchTestService;
import org.springframework.stereotype.Service;

/**
 * @author Soybeany
 * @date 2022/2/22
 */
@Service
public class BatchTestServiceImpl implements IBatchTestService {

    @Override
    public String getBatchValue(String input) {
        return "来自broker";
    }

}
