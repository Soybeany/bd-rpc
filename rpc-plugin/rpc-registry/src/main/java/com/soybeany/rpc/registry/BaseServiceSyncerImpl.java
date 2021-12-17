package com.soybeany.rpc.registry;

import com.soybeany.sync.core.api.IServerPlugin;
import com.soybeany.sync.server.BaseServerServiceImpl;

import java.util.List;

/**
 * @author Soybeany
 * @date 2021/12/16
 */
public abstract class BaseServiceSyncerImpl extends BaseServerServiceImpl {

    @Override
    protected void onSetupPlugins(List<IServerPlugin> plugins) {
        plugins.add(new RpcRegistryPlugin(onSetupAcceptableSystems(), this::onGetNewServiceManager));
    }

    // ***********************子类实现****************************

    protected abstract String[] onSetupAcceptableSystems();

    protected abstract IServiceManager onGetNewServiceManager(String system);

}
