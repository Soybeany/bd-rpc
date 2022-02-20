package com.demo.service1.model;

import lombok.RequiredArgsConstructor;

import java.io.Serializable;

/**
 * @author Soybeany
 * @date 2022/2/19
 */
@RequiredArgsConstructor
public class ResultImpl extends BaseResult implements Serializable {

    private final String v;

    @Override
    public String getValue() {
        return v;
    }
}
