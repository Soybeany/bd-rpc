package com.soybeany.mq.broker;

import com.soybeany.mq.broker.api.IStorageManager;
import com.soybeany.mq.broker.plugin.MqBrokerPluginProvider;
import com.soybeany.mq.core.api.IMqBrokerSyncUrlProvider;
import com.soybeany.sync.core.util.NetUtils;
import com.soybeany.sync.server.api.IServerPlugin;
import com.soybeany.sync.server.impl.BaseServerSyncerImpl;

import java.util.List;

/**
 * @author Soybeany
 * @date 2021/12/16
 */
public abstract class BaseMqBrokerSyncerImpl extends BaseServerSyncerImpl implements IMqBrokerSyncUrlProvider {

    @Override
    protected void onSetupPlugins(List<IServerPlugin<?, ?>> plugins) {
        plugins.addAll(MqBrokerPluginProvider.get(onSetupStorageManager()));
    }

    @Override
    public String onGetSyncUrl() {
        return onSetupSyncUrl(NetUtils.getLocalIpAddress());
    }

    // ***********************子类实现****************************

    protected abstract String onSetupSyncUrl(String ip);

    protected abstract IStorageManager onSetupStorageManager();

}
