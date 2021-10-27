package com.soybeany.rpc.model.resource;

import lombok.Data;

/**
 * @author Soybeany
 * @date 2021/10/27
 */
@Data
public class RpcInfo {

    /**
     * ip地址
     */
    private String address;

    /**
     * 端口号
     */
    private Integer port;

    /**
     * 用于匹配的版本
     */
    private Integer version;

    /**
     * 调用接口时使用的凭证
     */
    private String authorization;

}
