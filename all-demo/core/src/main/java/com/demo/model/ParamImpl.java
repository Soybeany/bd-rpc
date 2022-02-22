package com.demo.model;

import lombok.RequiredArgsConstructor;

import java.io.Serializable;

/**
 * @author Soybeany
 * @date 2022/2/19
 */
@RequiredArgsConstructor
public class ParamImpl implements Interface, Serializable {

    private final String v;

    @Override
    public String getValue() {
        return v;
    }
}
