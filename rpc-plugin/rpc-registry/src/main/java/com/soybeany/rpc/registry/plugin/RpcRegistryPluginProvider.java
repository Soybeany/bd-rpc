package com.soybeany.rpc.registry.plugin;

import com.soybeany.rpc.registry.api.IRpcStorageManager;
import com.soybeany.sync.server.api.IServerPlugin;

import java.util.Arrays;
import java.util.List;

/**
 * @author Soybeany
 * @date 2022/1/17
 */
public class RpcRegistryPluginProvider {

    public static List<IServerPlugin<?, ?>> get(IRpcStorageManager manager) {
        return Arrays.asList(
                new RpcRegistryPluginC(manager),
                new RpcRegistryPluginP(manager)
        );
    }

}
