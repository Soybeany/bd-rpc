package com.soybeany.rpc.model.param;

import com.soybeany.rpc.model.resource.ProviderResource;
import com.soybeany.rpc.model.resource.RpcInfo;
import com.soybeany.rpc.model.resource.RpcResource;
import lombok.Data;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Soybeany
 * @date 2021/10/26
 */
@Data
public class ProviderParam {

    /**
     * 调用信息
     */
    private RpcInfo info;

    /**
     * 提供的资源
     */
    private List<RpcResource> resources;

    // ***********************方法区****************************

    public List<ProviderResource> toResources() {
        List<ProviderResource> list = new LinkedList<>();
        Date date = new Date();
        resources.forEach(r -> list.add(ProviderResource.getNew(info, r, date)));
        return list;
    }

}
