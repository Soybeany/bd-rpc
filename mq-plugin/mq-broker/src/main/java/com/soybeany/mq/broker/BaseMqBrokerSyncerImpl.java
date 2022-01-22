package com.soybeany.mq.broker;

import com.soybeany.sync.core.api.IServerPlugin;
import com.soybeany.sync.server.BaseServiceSyncerImpl;

import java.util.List;

/**
 * @author Soybeany
 * @date 2021/12/16
 */
public abstract class BaseMqBrokerSyncerImpl extends BaseServiceSyncerImpl {

    @Override
    protected void onSetupPlugins(List<IServerPlugin<?, ?>> plugins) {
        plugins.addAll(MqBrokerPlugin.get(onSetupStorageManager()));
    }

    // ***********************子类实现****************************

    protected abstract IStorageManager onSetupStorageManager();

}
