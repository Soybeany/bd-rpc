package com.soybeany.mq.broker;

import com.soybeany.mq.core.model.BdMqConstants;
import com.soybeany.mq.core.model.MqBrokerInputR;
import com.soybeany.mq.core.model.MqBrokerOutputR;
import com.soybeany.sync.core.api.IClientPlugin;
import lombok.RequiredArgsConstructor;

/**
 * @author Soybeany
 * @date 2022/1/22
 */
@RequiredArgsConstructor
public class MqBrokerPluginR implements IClientPlugin<MqBrokerInputR, MqBrokerOutputR> {

    private final String system;
    private final String syncUrl;

    @Override
    public String onSetupSyncTagToHandle() {
        return BdMqConstants.TAG_B_R;
    }

    @Override
    public Class<MqBrokerInputR> onGetInputClass() {
        return MqBrokerInputR.class;
    }

    @Override
    public Class<MqBrokerOutputR> onGetOutputClass() {
        return MqBrokerOutputR.class;
    }

    @Override
    public boolean onBeforeSync(String uid, MqBrokerOutputR output) throws Exception {
        output.setSystem(system);
        output.setSyncUrl(syncUrl);
        return IClientPlugin.super.onBeforeSync(uid, output);
    }

}
