package com.soybeany.rpc.registry;

import com.soybeany.rpc.core.model.ServerInfo;

import java.util.Set;

/**
 * @author Soybeany
 * @date 2021/11/1
 */
public interface IStorageManager {

    Set<ServerInfo> load(String id);

    void save(ServerInfo info, Set<String> serviceIds);

}
