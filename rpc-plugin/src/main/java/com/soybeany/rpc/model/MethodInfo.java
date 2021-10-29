package com.soybeany.rpc.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Soybeany
 * @date 2021/10/29
 */
@Data
@AllArgsConstructor
public class MethodInfo {

    private String serviceId;
    private String methodName;
    private String[] clazzNames;
    private Object[] args;

}
