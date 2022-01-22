package com.soybeany.mq.broker;

import com.soybeany.mq.core.model.BdMqConstants;
import com.soybeany.mq.core.model.registry.MqBrokerInput;
import com.soybeany.mq.core.model.registry.MqBrokerOutput;
import com.soybeany.sync.core.api.IClientPlugin;
import lombok.RequiredArgsConstructor;

/**
 * @author Soybeany
 * @date 2022/1/22
 */
@RequiredArgsConstructor
public class MqBrokerPluginR implements IClientPlugin<MqBrokerInput, MqBrokerOutput> {

    private final String system;
    private final String syncUrl;

    @Override
    public String onSetupSyncTagToHandle() {
        return BdMqConstants.TAG_B_R;
    }

    @Override
    public Class<MqBrokerInput> onGetInputClass() {
        return MqBrokerInput.class;
    }

    @Override
    public Class<MqBrokerOutput> onGetOutputClass() {
        return MqBrokerOutput.class;
    }

    @Override
    public boolean onBeforeSync(String uid, MqBrokerOutput output) throws Exception {
        output.setSystem(system);
        output.setSyncUrl(syncUrl);
        return IClientPlugin.super.onBeforeSync(uid, output);
    }

}
