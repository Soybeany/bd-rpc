package com.soybeany.rpc.model;

import lombok.Data;

/**
 * @author Soybeany
 * @date 2021/10/27
 */
@Data
public class RpcResource {

    /**
     * 资源的唯一标识
     */
    private String id;

    /**
     * 请求路径
     */
    private String path;

    // ***********************方法区****************************

    protected void copy(RpcResource r) {
        this.id = r.id;
        this.path = r.path;
    }

}
