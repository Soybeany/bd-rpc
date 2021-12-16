package com.soybeany.rpc.core.model;

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

}
