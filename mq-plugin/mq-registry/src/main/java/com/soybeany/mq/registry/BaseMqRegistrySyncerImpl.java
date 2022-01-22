package com.soybeany.mq.registry;

import com.soybeany.sync.core.api.IServerPlugin;
import com.soybeany.sync.server.BaseServerSyncerImpl;

import java.util.List;

/**
 * @author Soybeany
 * @date 2021/12/16
 */
public abstract class BaseMqRegistrySyncerImpl extends BaseServerSyncerImpl {

    @Override
    protected void onSetupPlugins(List<IServerPlugin<?, ?>> plugins) {
        plugins.addAll(MqRegistryPlugin.get(onSetupStorageManager()));
    }

    // ***********************子类实现****************************

    protected abstract IStorageManager onSetupStorageManager();

}
