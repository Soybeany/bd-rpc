package com.demo.service2.impl;

import com.demo.model.ExIoException;
import com.demo.service.IExceptionTestService;
import org.springframework.stereotype.Service;

/**
 * @author Soybeany
 * @date 2022/2/22
 */
@Service
public class ExceptionTestServiceImpl implements IExceptionTestService {
    @Override
    public String getValue() {
        throw new RuntimeException("自定义1");
    }

    @Override
    public String getValue2() throws Exception {
        throw new Exception("自定义2");
    }

    @Override
    public String getValue3() throws ExIoException {
        throw new ExIoException("自定义3");
    }
}
