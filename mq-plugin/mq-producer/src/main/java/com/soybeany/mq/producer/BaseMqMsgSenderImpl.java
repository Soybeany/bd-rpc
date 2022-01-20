package com.soybeany.mq.producer;

import com.soybeany.mq.core.api.IMqMsgSendCallback;
import com.soybeany.mq.core.api.IMqMsgSender;
import com.soybeany.mq.core.model.MqProducerMsg;
import com.soybeany.sync.client.BaseClientServiceImpl;
import com.soybeany.sync.core.api.IClientPlugin;

import java.util.List;

/**
 * @author Soybeany
 * @date 2021/12/16
 */
public abstract class BaseMqMsgSenderImpl extends BaseClientServiceImpl implements IMqMsgSender {

    private MqProducerPlugin plugin;

    @Override
    protected void onSetupPlugins(List<IClientPlugin<?, ?>> plugins) {
        plugins.add(plugin = new MqProducerPlugin());
    }

    @Override
    public void send(String topic, MqProducerMsg msg, IMqMsgSendCallback callback) {
        plugin.send(topic, msg, callback);
    }
}
