package com.soybeany.rpc.core.model;

import lombok.Data;

/**
 * @author Soybeany
 * @date 2021/10/27
 */
@Data
public class ServerInfo {

    /**
     * 同步的url
     */
    private String syncUrl;

    /**
     * 调用接口时使用的凭证
     */
    private String authorization = "";

}
