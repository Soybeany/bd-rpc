package com.soybeany.rpc.registry;

import com.soybeany.rpc.registry.api.IRpcStorageManager;
import com.soybeany.rpc.registry.plugin.RpcRegistryPluginProvider;
import com.soybeany.sync.server.api.IServerPlugin;
import com.soybeany.sync.server.impl.BaseServerSyncerImpl;

import java.util.List;

/**
 * @author Soybeany
 * @date 2021/12/16
 */
public abstract class BaseRpcRegistrySyncerImpl extends BaseServerSyncerImpl {

    @Override
    protected void onSetupPlugins(List<IServerPlugin<?, ?>> plugins) {
        plugins.addAll(RpcRegistryPluginProvider.get(onSetupStorageManager()));
    }

    // ***********************子类实现****************************

    protected abstract IRpcStorageManager onSetupStorageManager();

}
