package com.soybeany.rpc.model.resource;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author Soybeany
 * @date 2021/10/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProviderResource extends RpcResource {

    /**
     * 调用信息
     */
    private RpcInfo info;

    /**
     * 同步时间
     */
    private Date syncTime;

    // ***********************方法区****************************

    public static ProviderResource getNew(RpcInfo info, RpcResource r, Date syncTime) {
        ProviderResource resource = new ProviderResource();
        resource.info = info;
        resource.syncTime = syncTime;
        resource.copy(r);
        return resource;
    }

}
