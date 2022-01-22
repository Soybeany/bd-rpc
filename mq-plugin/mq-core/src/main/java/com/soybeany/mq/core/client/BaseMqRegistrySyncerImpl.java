package com.soybeany.mq.core.client;

import com.soybeany.mq.core.api.IMqClientInputRListener;
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

    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired(required = false)
    private IMqClientInputRListener listener;

    @Override
    protected void onSetupPlugins(List<IClientPlugin<?, ?>> plugins) {
        plugins.add(new MqClientPlugin(onSetupSystem(), listener));
    }

}
