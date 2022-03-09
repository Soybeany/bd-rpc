package com.soybeany.mq.producer.impl;

import com.soybeany.mq.core.model.MqProducerMsg;
import com.soybeany.mq.producer.api.IMqMsgSender;
import com.soybeany.mq.producer.plugin.MqProducerPlugin;
import com.soybeany.rpc.client.api.IRpcOtherPluginsProvider;
import com.soybeany.sync.client.api.IClientPlugin;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @author Soybeany
 * @date 2022/3/9
 */
public class MqPluginProvider implements IRpcOtherPluginsProvider, IMqMsgSender {

    private MqProducerPlugin plugin;

    @Override
    public <T extends Serializable> void send(String topic, MqProducerMsg<T> msg) {
        plugin.send(topic, msg);
    }

    @Override
    public List<IClientPlugin<?, ?>> onSetupPlugins() {
        return Collections.singletonList(plugin = new MqProducerPlugin());
    }

}
