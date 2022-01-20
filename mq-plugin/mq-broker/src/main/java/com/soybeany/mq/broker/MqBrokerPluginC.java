package com.soybeany.mq.broker;

import com.soybeany.mq.core.model.BdMqConstants;
import com.soybeany.mq.core.model.MqConsumerInput;
import com.soybeany.mq.core.model.MqConsumerOutput;
import com.soybeany.sync.core.exception.SyncException;

/**
 * @author Soybeany
 * @date 2022/1/19
 */
class MqBrokerPluginC extends MqBrokerPlugin<MqConsumerOutput, MqConsumerInput> {

    @Override
    public String onSetupSyncTagToHandle() {
        return BdMqConstants.TAG_C;
    }

    @Override
    public Class<MqConsumerOutput> onGetInputClass() {
        return MqConsumerOutput.class;
    }

    @Override
    public Class<MqConsumerInput> onGetOutputClass() {
        return MqConsumerInput.class;
    }

    @Override
    public void onHandleSync(MqConsumerOutput mqConsumerOutput, MqConsumerInput mqConsumerInput) throws SyncException {

    }
}
