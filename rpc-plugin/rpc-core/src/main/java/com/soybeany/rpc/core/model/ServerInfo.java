package com.soybeany.rpc.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Soybeany
 * @date 2021/10/27
 */
@Data
@EqualsAndHashCode(exclude = "authorization")
public class ServerInfo {

    /**
     * 使用的协议
     */
    private String protocol = "http";

    /**
     * ip地址
     */
    private String address;

    /**
     * 端口号
     */
    private Integer port;

    /**
     * 上下文路径
     */
    private String contextPath;

    /**
     * 请求尾缀
     */
    private String suffix;

    /**
     * 调用接口时使用的凭证
     */
    private String authorization = "";

    public String toUrl(String path) {
        return getProtocol() + "://" + getAddress() + ":" + getPort() + getContextPath() + path + (null != getSuffix() ? getSuffix() : "");
    }

}
