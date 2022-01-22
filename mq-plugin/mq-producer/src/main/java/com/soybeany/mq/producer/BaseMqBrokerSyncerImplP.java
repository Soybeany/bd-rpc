package com.soybeany.mq.producer;

import com.soybeany.mq.core.api.IMqClientInputRListener;
import com.soybeany.mq.core.api.IMqMsgSendCallback;
import com.soybeany.mq.core.api.IMqMsgSender;
import com.soybeany.mq.core.model.broker.MqProducerMsg;
import com.soybeany.mq.core.model.registry.MqClientInput;
import com.soybeany.sync.client.BaseClientSyncerImpl;
import com.soybeany.sync.core.api.IClientPlugin;

import java.util.List;

/**
 * @author Soybeany
 * @date 2022/1/22
 */
public abstract class BaseMqBrokerSyncerImplP extends BaseClientSyncerImpl implements IMqMsgSender, IMqClientInputRListener {

    private MqProducerPlugin plugin;

    @Override
    protected void onSetupPlugins(List<IClientPlugin<?, ?>> plugins) {
        plugins.add(plugin = new MqProducerPlugin());
    }

    @Override
    public void asyncSend(String topic, MqProducerMsg msg, IMqMsgSendCallback callback) {
        plugin.asyncSend(topic, msg, callback);
    }

    @Override
    public void onReceiveInputR(MqClientInput input) {
        service.getUrlPicker().set(input.getBrokersSyncUrl());
    }

}
