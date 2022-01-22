package com.soybeany.mq.consumer;

import com.soybeany.mq.core.api.IMqClientInputRListener;
import com.soybeany.mq.core.api.IMqMsgHandler;
import com.soybeany.mq.core.model.MqClientInputR;
import com.soybeany.sync.client.BaseClientServiceImpl;
import com.soybeany.sync.core.api.IClientPlugin;

import java.util.List;

/**
 * @author Soybeany
 * @date 2021/12/16
 */
public abstract class BaseMqConsumerImplB extends BaseClientServiceImpl implements IMqClientInputRListener {

    @Override
    protected void onSetupPlugins(List<IClientPlugin<?, ?>> plugins) {
        plugins.add(new MqConsumerPluginB(onSetupMsgHandlers(), onSetupExceptionHandler()));
    }

    @Override
    public void onReceiveInputR(MqClientInputR input) {
        service.getUrlPicker().set(input.getBrokersSyncUrl());
    }

    // ***********************子类实现****************************

    protected abstract List<IMqMsgHandler> onSetupMsgHandlers();

    protected abstract IMqExceptionHandler onSetupExceptionHandler();
}
