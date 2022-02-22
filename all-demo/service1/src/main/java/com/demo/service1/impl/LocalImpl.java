package com.demo.service1.impl;

import com.demo.service.ILocalService;
import org.springframework.stereotype.Component;

/**
 * @author Soybeany
 * @date 2021/11/1
 */
@Component
public class LocalImpl implements ILocalService {

    @Override
    public String getValue() {
        return "service1本地实现";
    }

}
