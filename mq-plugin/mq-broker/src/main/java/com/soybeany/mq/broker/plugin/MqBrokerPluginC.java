package com.soybeany.mq.broker.plugin;

import com.soybeany.mq.broker.api.IStorageManager;
import com.soybeany.mq.core.model.BdMqConstants;
import com.soybeany.mq.core.model.MqConsumerInput;
import com.soybeany.mq.core.model.MqConsumerOutput;
import com.soybeany.sync.core.exception.SyncException;
import com.soybeany.sync.server.api.IServerPlugin;
import lombok.RequiredArgsConstructor;

/**
 * @author Soybeany
 * @date 2022/1/19
 */
@RequiredArgsConstructor
public class MqBrokerPluginC implements IServerPlugin<MqConsumerOutput, MqConsumerInput> {

    private final IStorageManager storageManager;

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
    public void onHandleSync(String clientIp, MqConsumerOutput in, MqConsumerInput out) throws SyncException {
        try {
            out.getMessages().putAll(storageManager.load(in.getTopics()));
        } catch (Exception e) {
            throw new SyncException(e.getMessage());
        }
    }
}
