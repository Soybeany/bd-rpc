package com.soybeany.rpc.model.resource;

import lombok.Data;

/**
 * @author Soybeany
 * @date 2021/10/27
 */
@Data
public class RpcResource {

    /**
     * 关联的标签
     */
    private String tag;

    /**
     * 请求路径
     */
    private String path;

    // ***********************方法区****************************

    protected void copy(RpcResource r) {
        this.tag = r.tag;
        this.path = r.path;
    }

}
