package com.soybeany.rpc.registry.plugin;

import com.soybeany.rpc.registry.api.IRpcStorageManager;
import com.soybeany.sync.core.api.IServerPlugin;
import com.soybeany.sync.core.model.BaseClientOutput;

import java.util.Arrays;
import java.util.List;

/**
 * @author Soybeany
 * @date 2022/1/17
 */
public abstract class RpcRegistryPlugin<Input extends BaseClientOutput, Output> implements IServerPlugin<Input, Output> {

    public static List<IServerPlugin<?, ?>> get(IRpcStorageManager manager) {
        return Arrays.asList(
                new RpcRegistryPluginC(manager),
                new RpcRegistryPluginP(manager)
        );
    }

}
