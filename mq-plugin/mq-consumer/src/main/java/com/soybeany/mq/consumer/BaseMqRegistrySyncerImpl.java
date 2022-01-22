package com.soybeany.mq.consumer;

import com.soybeany.mq.core.api.IMqClientInputRListener;
import com.soybeany.mq.core.plugin.MqClientPluginR;
import com.soybeany.sync.client.BaseClientSyncerImpl;
import com.soybeany.sync.core.api.IClientPlugin;
import com.soybeany.sync.core.api.ISystemConfig;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Soybeany
 * @date 2022/1/22
 */
public abstract class BaseMqRegistrySyncerImpl extends BaseClientSyncerImpl implements ISystemConfig {

    @Autowired(required = false)
    private IMqClientInputRListener listener;

    @Override
    protected void onSetupPlugins(List<IClientPlugin<?, ?>> plugins) {
        plugins.add(new MqClientPluginR(onSetupSystem(), listener));
    }

}
