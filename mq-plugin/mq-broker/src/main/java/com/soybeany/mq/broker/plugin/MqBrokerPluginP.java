package com.soybeany.mq.broker.plugin;

import com.soybeany.mq.broker.api.IStorageManager;
import com.soybeany.mq.core.model.BdMqConstants;
import com.soybeany.mq.core.model.MqProducerInput;
import com.soybeany.mq.core.model.MqProducerOutput;
import lombok.RequiredArgsConstructor;

/**
 * @author Soybeany
 * @date 2022/1/19
 */
@RequiredArgsConstructor
class MqBrokerPluginP extends MqBrokerPlugin<MqProducerOutput, MqProducerInput> {

    private final IStorageManager storageManager;

    @Override
    public String onSetupSyncTagToHandle() {
        return BdMqConstants.TAG_P;
    }

    @Override
    public Class<MqProducerOutput> onGetInputClass() {
        return MqProducerOutput.class;
    }

    @Override
    public Class<MqProducerInput> onGetOutputClass() {
        return MqProducerInput.class;
    }

    @Override
    public void onHandleSync(String clientIp, MqProducerOutput in, MqProducerInput out) {
        try {
            storageManager.save(in.getMessages());
            out.setSuccess(true);
        } catch (Exception e) {
            out.setMsg(e.getMessage());
        }
    }
}
