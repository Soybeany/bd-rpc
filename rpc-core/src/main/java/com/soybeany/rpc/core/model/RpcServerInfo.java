package com.soybeany.rpc.core.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Soybeany
 * @date 2021/10/27
 */
@Data
public class RpcServerInfo implements Serializable {

    /**
     * 服务器分组
     */
    String group;

    /**
     * 调用的url
     */
    private String invokeUrl;

    /**
     * 调用接口时使用的凭证
     */
    private String authorization = "";

}
