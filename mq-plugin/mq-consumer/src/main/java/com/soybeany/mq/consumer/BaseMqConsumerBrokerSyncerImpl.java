package com.soybeany.mq.consumer;

import com.soybeany.mq.consumer.api.IMqExceptionHandler;
import com.soybeany.mq.consumer.api.IMqMsgHandler;
import com.soybeany.mq.consumer.api.ITopicInfoRepository;
import com.soybeany.mq.consumer.plugin.MqConsumerPlugin;
import com.soybeany.mq.core.api.IMqBrokerSyncUrlProvider;
import com.soybeany.sync.client.BaseClientSyncerImpl;
import com.soybeany.sync.core.api.IClientPlugin;
import com.soybeany.sync.core.picker.DataPicker;
import com.soybeany.sync.core.picker.DataPickerSupplierImpl;

import java.util.List;

/**
 * @author Soybeany
 * @date 2021/12/16
 */
public abstract class BaseMqConsumerBrokerSyncerImpl extends BaseClientSyncerImpl {

    @Override
    public DataPicker<String> onSetupSyncServerPicker() {
        return new DataPickerSupplierImpl<>(() -> onGetMqBrokerSyncUrlProvider().onGetSyncUrl());
    }

    @Override
    protected void onSetupPlugins(List<IClientPlugin<?, ?>> plugins) {
        plugins.add(new MqConsumerPlugin(onSetupMsgHandlers(), onSetupTopicInfoRepository(), onSetupExceptionHandler()));
    }

    // ***********************子类实现****************************

    protected abstract IMqBrokerSyncUrlProvider onGetMqBrokerSyncUrlProvider();

    protected abstract List<IMqMsgHandler> onSetupMsgHandlers();

    protected abstract ITopicInfoRepository onSetupTopicInfoRepository();

    protected abstract IMqExceptionHandler onSetupExceptionHandler();
}
