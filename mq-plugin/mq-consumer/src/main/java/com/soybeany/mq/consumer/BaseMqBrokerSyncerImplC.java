package com.soybeany.mq.consumer;

import com.soybeany.mq.core.api.IMqClientInputRListener;
import com.soybeany.mq.core.api.IMqMsgHandler;
import com.soybeany.mq.core.model.registry.MqClientInput;
import com.soybeany.sync.client.BaseClientSyncerImpl;
import com.soybeany.sync.core.api.IClientPlugin;

import java.util.List;

/**
 * @author Soybeany
 * @date 2021/12/16
 */
public abstract class BaseMqBrokerSyncerImplC extends BaseClientSyncerImpl implements IMqClientInputRListener {

    @Override
    protected void onSetupPlugins(List<IClientPlugin<?, ?>> plugins) {
        plugins.add(new MqConsumerPlugin(onSetupMsgHandlers(), onSetupExceptionHandler()));
    }

    @Override
    public void onReceiveInputR(MqClientInput input) {
        service.getUrlPicker().set(input.getBrokersSyncUrl());
    }

    // ***********************子类实现****************************

    protected abstract List<IMqMsgHandler> onSetupMsgHandlers();

    protected abstract IMqExceptionHandler onSetupExceptionHandler();
}
