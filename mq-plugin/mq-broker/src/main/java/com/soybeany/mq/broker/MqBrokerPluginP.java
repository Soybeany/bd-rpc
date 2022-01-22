package com.soybeany.mq.broker;

import com.soybeany.mq.core.model.BdMqConstants;
import com.soybeany.mq.core.model.MqProducerInputB;
import com.soybeany.mq.core.model.MqProducerOutputB;
import lombok.RequiredArgsConstructor;

/**
 * @author Soybeany
 * @date 2022/1/19
 */
@RequiredArgsConstructor
class MqBrokerPluginP extends MqBrokerPlugin<MqProducerOutputB, MqProducerInputB> {

    private final IStorageManager storageManager;

    @Override
    public String onSetupSyncTagToHandle() {
        return BdMqConstants.TAG_P_B;
    }

    @Override
    public Class<MqProducerOutputB> onGetInputClass() {
        return MqProducerOutputB.class;
    }

    @Override
    public Class<MqProducerInputB> onGetOutputClass() {
        return MqProducerInputB.class;
    }

    @Override
    public void onHandleSync(MqProducerOutputB in, MqProducerInputB out) {
        try {
            storageManager.save(in.getMessages());
            out.setSuccess(true);
        } catch (Exception e) {
            out.setMsg(e.getMessage());
        }
    }
}
