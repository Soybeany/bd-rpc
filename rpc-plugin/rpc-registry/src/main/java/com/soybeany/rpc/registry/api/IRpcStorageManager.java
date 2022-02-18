package com.soybeany.rpc.registry.api;

import com.soybeany.rpc.core.model.RpcServerInfo;

import java.util.Set;

/**
 * @author Soybeany
 * @date 2021/11/1
 */
public interface IRpcStorageManager {

    Set<RpcServerInfo> load(String system, String serviceId);

    void save(String system, RpcServerInfo info, Set<String> serviceIds);

}
