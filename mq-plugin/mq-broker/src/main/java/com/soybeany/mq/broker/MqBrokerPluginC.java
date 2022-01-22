package com.soybeany.mq.broker;

import com.soybeany.mq.core.model.BdMqConstants;
import com.soybeany.mq.core.model.MqConsumerInputB;
import com.soybeany.mq.core.model.MqConsumerOutputB;
import com.soybeany.sync.core.exception.SyncException;
import lombok.RequiredArgsConstructor;

/**
 * @author Soybeany
 * @date 2022/1/19
 */
@RequiredArgsConstructor
class MqBrokerPluginC extends MqBrokerPlugin<MqConsumerOutputB, MqConsumerInputB> {

    private final IStorageManager storageManager;

    @Override
    public String onSetupSyncTagToHandle() {
        return BdMqConstants.TAG_C_B;
    }

    @Override
    public Class<MqConsumerOutputB> onGetInputClass() {
        return MqConsumerOutputB.class;
    }

    @Override
    public Class<MqConsumerInputB> onGetOutputClass() {
        return MqConsumerInputB.class;
    }

    @Override
    public void onHandleSync(MqConsumerOutputB in, MqConsumerInputB out) throws SyncException {
        try {
            out.getMessages().putAll(storageManager.load(in.getTopics()));
        } catch (Exception e) {
            throw new SyncException(e.getMessage());
        }
    }
}
