package com.soybeany.rpc.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author Soybeany
 * @date 2021/10/27
 */
@EqualsAndHashCode(exclude = "syncTime")
@Data
public class ProviderResource {

    /**
     * 资源id
     */
    private String id;

    /**
     * 服务器信息
     */
    private ServerInfo info;

    /**
     * 同步时间
     */
    private Date syncTime;

    // ***********************方法区****************************

    public static ProviderResource getNew(String id, ServerInfo info, Date syncTime) {
        ProviderResource resource = new ProviderResource();
        resource.id = id;
        resource.info = info;
        resource.syncTime = syncTime;
        return resource;
    }

}
