package com.soybeany.rpc.registry;

import com.soybeany.sync.core.api.IServerPlugin;
import com.soybeany.sync.server.BaseServerSyncerImpl;

import java.util.List;

/**
 * @author Soybeany
 * @date 2021/12/16
 */
public abstract class BaseRpcRegistrySyncerImplR extends BaseServerSyncerImpl {

    @Override
    protected void onSetupPlugins(List<IServerPlugin<?, ?>> plugins) {
        plugins.addAll(RpcRegistryPlugin.get(onSetupStorageManager()));
    }

    // ***********************子类实现****************************

    protected abstract IRpcStorageManager onSetupStorageManager();

}
