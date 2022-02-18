package com.soybeany.mq.producer;

import com.soybeany.mq.core.api.IMqBrokerSyncUrlProvider;
import com.soybeany.mq.core.model.MqProducerMsg;
import com.soybeany.mq.producer.api.IMqMsgSendCallback;
import com.soybeany.mq.producer.api.IMqMsgSender;
import com.soybeany.mq.producer.plugin.MqProducerPlugin;
import com.soybeany.sync.client.api.IClientPlugin;
import com.soybeany.sync.client.impl.BaseClientSyncerImpl;
import com.soybeany.sync.client.picker.DataPicker;
import com.soybeany.sync.client.picker.DataPickerSupplierImpl;

import java.util.List;

/**
 * @author Soybeany
 * @date 2022/1/22
 */
public abstract class BaseMqProducerBrokerSyncerImpl extends BaseClientSyncerImpl implements IMqMsgSender {

    private MqProducerPlugin plugin;

    @Override
    public DataPicker<String> onSetupSyncServerPicker() {
        return new DataPickerSupplierImpl<>(() -> onGetMqBrokerSyncUrlProvider().onGetSyncUrl());
    }

    @Override
    protected void onSetupPlugins(List<IClientPlugin<?, ?>> plugins) {
        plugins.add(plugin = new MqProducerPlugin());
    }

    @Override
    public void asyncSend(String topic, MqProducerMsg msg, IMqMsgSendCallback callback) {
        plugin.asyncSend(topic, msg, callback);
    }

    protected abstract IMqBrokerSyncUrlProvider onGetMqBrokerSyncUrlProvider();

}
