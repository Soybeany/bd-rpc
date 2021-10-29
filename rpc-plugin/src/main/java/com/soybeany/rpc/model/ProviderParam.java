package com.soybeany.rpc.model;

import lombok.Data;

import java.util.*;

/**
 * @author Soybeany
 * @date 2021/10/26
 */
@Data
public class ProviderParam {

    /**
     * 服务器信息
     */
    private ServerInfo info;

    /**
     * 提供的资源id列表
     */
    private Set<String> serviceIds;

    // ***********************方法区****************************

    public Set<ProviderResource> toResources() {
        Set<ProviderResource> set = new HashSet<>();
        Date date = new Date();
        serviceIds.forEach(id -> set.add(ProviderResource.getNew(id, info, date)));
        return set;
    }

}
