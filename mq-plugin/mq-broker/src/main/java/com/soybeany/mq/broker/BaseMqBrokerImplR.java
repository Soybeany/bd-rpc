package com.soybeany.mq.broker;

import com.soybeany.sync.client.BaseClientServiceImpl;
import com.soybeany.sync.core.api.IClientPlugin;
import com.soybeany.sync.core.api.ISystemConfig;
import com.soybeany.sync.core.util.NetUtils;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * @author Soybeany
 * @date 2021/12/16
 */
public abstract class BaseMqBrokerImplR extends BaseClientServiceImpl implements ISystemConfig {

    @Override
    protected void onSetupPlugins(List<IClientPlugin<?, ?>> plugins) {
        plugins.add(new MqBrokerPluginR(onSetupSystem(), onSetupSyncUrl(NetUtils.getLocalIpAddress())));
    }

    @NonNull
    protected abstract String onSetupSyncUrl(String ip);

}
