package com.soybeany.rpc.registry;

import com.soybeany.sync.core.api.IServerPlugin;
import com.soybeany.sync.server.BaseServiceSyncerImpl;

import java.util.List;

/**
 * @author Soybeany
 * @date 2021/12/16
 */
public abstract class BaseRpcServiceSyncerImpl extends BaseServiceSyncerImpl {

    @Override
    protected void onSetupPlugins(List<IServerPlugin<?, ?>> plugins) {
        plugins.addAll(RpcRegistryPlugin.get(onSetupAcceptableSystems(), this::onGetNewServiceManager));
    }

    // ***********************子类实现****************************

    protected String[] onSetupAcceptableSystems() {
        return null;
    }

    protected abstract IServiceManager onGetNewServiceManager(String system);

}
