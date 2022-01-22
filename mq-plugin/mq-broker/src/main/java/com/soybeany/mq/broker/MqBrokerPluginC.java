package com.soybeany.mq.broker;

import com.soybeany.mq.core.model.BdMqConstants;
import com.soybeany.mq.core.model.broker.MqConsumerInput;
import com.soybeany.mq.core.model.broker.MqConsumerOutput;
import com.soybeany.sync.core.exception.SyncException;
import lombok.RequiredArgsConstructor;

/**
 * @author Soybeany
 * @date 2022/1/19
 */
@RequiredArgsConstructor
class MqBrokerPluginC extends MqBrokerPlugin<MqConsumerOutput, MqConsumerInput> {

    private final IStorageManager storageManager;

    @Override
    public String onSetupSyncTagToHandle() {
        return BdMqConstants.TAG_C_B;
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
    public void onHandleSync(MqConsumerOutput in, MqConsumerInput out) throws SyncException {
        try {
            out.getMessages().putAll(storageManager.load(in.getTopics()));
        } catch (Exception e) {
            throw new SyncException(e.getMessage());
        }
    }
}
