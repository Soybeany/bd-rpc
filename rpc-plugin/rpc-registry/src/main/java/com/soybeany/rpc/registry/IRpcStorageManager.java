package com.soybeany.rpc.registry;

import com.soybeany.rpc.core.model.ServerInfo;

import java.util.Set;

/**
 * @author Soybeany
 * @date 2021/11/1
 */
public interface IRpcStorageManager {

    Set<ServerInfo> load(String system, String serviceId);

    void save(String system, ServerInfo info, Set<String> serviceIds);

}
