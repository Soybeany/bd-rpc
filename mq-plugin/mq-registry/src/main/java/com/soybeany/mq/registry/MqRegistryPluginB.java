package com.soybeany.mq.registry;

import com.soybeany.mq.core.model.BdMqConstants;
import com.soybeany.mq.core.model.registry.MqBrokerInput;
import com.soybeany.mq.core.model.registry.MqBrokerOutput;
import lombok.RequiredArgsConstructor;

/**
 * @author Soybeany
 * @date 2022/1/19
 */
@RequiredArgsConstructor
class MqRegistryPluginB extends MqRegistryPlugin<MqBrokerOutput, MqBrokerInput> {

    private final IMqStorageManager storageManager;

    @Override
    public String onSetupSyncTagToHandle() {
        return BdMqConstants.TAG_B_R;
    }

    @Override
    public Class<MqBrokerOutput> onGetInputClass() {
        return MqBrokerOutput.class;
    }

    @Override
    public Class<MqBrokerInput> onGetOutputClass() {
        return MqBrokerInput.class;
    }

    @Override
    public void onHandleSync(MqBrokerOutput in, MqBrokerInput out) {
        storageManager.save(in.getSystem(), in.getSyncUrl());
    }

}
