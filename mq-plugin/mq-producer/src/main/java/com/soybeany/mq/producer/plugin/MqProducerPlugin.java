package com.soybeany.mq.producer.plugin;

import com.soybeany.mq.client.plugin.BaseMqClientRegistryPlugin;
import com.soybeany.mq.core.api.IMqMsgStorageManager;
import com.soybeany.mq.core.model.MqProducerMsg;
import com.soybeany.mq.producer.api.IMqMsgSender;
import com.soybeany.rpc.consumer.api.IRpcServiceProxy;
import com.soybeany.sync.client.api.IClientPlugin;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;

/**
 * @author Soybeany
 * @date 2022/1/19
 */
@Slf4j
public class MqProducerPlugin extends BaseMqClientRegistryPlugin implements IMqMsgSender {

    private IRpcServiceProxy proxy;
    private IMqMsgStorageManager mqMsgStorageManager;

    // ***********************子类实现****************************

    @Override
    public void onSetupRpcServiceProxy(IRpcServiceProxy proxy) {
        this.proxy = proxy;
    }

    @Override
    public void onAfterStartup(List<IClientPlugin<Object, Object>> appliedPlugins) {
        super.onAfterStartup(appliedPlugins);
        mqMsgStorageManager = proxy.get(IMqMsgStorageManager.class);
    }

    @Override
    public <T extends Serializable> void send(String topic, MqProducerMsg<T> msg) {
        mqMsgStorageManager.save(topic, msg);
    }

}
