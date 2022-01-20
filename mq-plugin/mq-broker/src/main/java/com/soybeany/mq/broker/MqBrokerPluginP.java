package com.soybeany.mq.broker;

import com.soybeany.mq.core.model.BdMqConstants;
import com.soybeany.mq.core.model.MqProducerInput;
import com.soybeany.mq.core.model.MqProducerOutput;
import com.soybeany.sync.core.exception.SyncException;

/**
 * @author Soybeany
 * @date 2022/1/19
 */
class MqBrokerPluginP extends MqBrokerPlugin<MqProducerOutput, MqProducerInput> {

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
    public void onHandleSync(MqProducerOutput mqProducerOutput, MqProducerInput mqProducerInput) throws SyncException {

    }

}
