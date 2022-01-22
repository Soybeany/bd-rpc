package com.soybeany.mq.registry;

import com.soybeany.mq.core.model.BdMqConstants;
import com.soybeany.mq.core.model.MqBrokerInputR;
import com.soybeany.mq.core.model.MqBrokerOutputR;
import lombok.RequiredArgsConstructor;

/**
 * @author Soybeany
 * @date 2022/1/19
 */
@RequiredArgsConstructor
class MqRegistryPluginB extends MqRegistryPlugin<MqBrokerOutputR, MqBrokerInputR> {

    private final IStorageManager storageManager;

    @Override
    public String onSetupSyncTagToHandle() {
        return BdMqConstants.TAG_B_R;
    }

    @Override
    public Class<MqBrokerOutputR> onGetInputClass() {
        return MqBrokerOutputR.class;
    }

    @Override
    public Class<MqBrokerInputR> onGetOutputClass() {
        return MqBrokerInputR.class;
    }

    @Override
    public void onHandleSync(MqBrokerOutputR in, MqBrokerInputR out) {
        storageManager.save(in.getSystem(), in.getSyncUrl());
    }

}
