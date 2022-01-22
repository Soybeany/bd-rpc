package com.soybeany.mq.producer;

import com.soybeany.mq.core.api.IMqClientInputRListener;
import com.soybeany.mq.core.api.IMqMsgSendCallback;
import com.soybeany.mq.core.api.IMqMsgSender;
import com.soybeany.mq.core.model.MqClientInputR;
import com.soybeany.mq.core.model.MqProducerMsgB;
import com.soybeany.sync.client.BaseClientServiceImpl;
import com.soybeany.sync.core.api.IClientPlugin;

import java.util.List;

/**
 * @author Soybeany
 * @date 2022/1/22
 */
public abstract class BaseMqMsgSenderImplB extends BaseClientServiceImpl implements IMqMsgSender, IMqClientInputRListener {

    private MqProducerPluginB plugin;

    @Override
    protected void onSetupPlugins(List<IClientPlugin<?, ?>> plugins) {
        plugins.add(plugin = new MqProducerPluginB());
    }

    @Override
    public void asyncSend(String topic, MqProducerMsgB msg, IMqMsgSendCallback callback) {
        plugin.asyncSend(topic, msg, callback);
    }

    @Override
    public void onReceiveInputR(MqClientInputR input) {
        service.getUrlPicker().set(input.getBrokersSyncUrl());
    }

}
