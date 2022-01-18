package com.soybeany.rpc.core.model;

import lombok.Data;

/**
 * @author Soybeany
 * @date 2021/10/27
 */
@Data
public class ServerInfo {

    /**
     * 服务器标签
     */
    String tag;

    /**
     * 调用的url
     */
    private String invokeUrl;

    /**
     * 调用接口时使用的凭证
     */
    private String authorization = "";

}
