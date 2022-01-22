package com.soybeany.mq.core.plugin;

import com.soybeany.mq.core.api.IMqClientInputRListener;
import com.soybeany.mq.core.model.BdMqConstants;
import com.soybeany.mq.core.model.MqClientInputR;
import com.soybeany.mq.core.model.MqClientOutputR;
import com.soybeany.sync.core.api.IClientPlugin;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Soybeany
 * @date 2022/1/21
 */
public class MqClientPluginR implements IClientPlugin<MqClientInputR, MqClientOutputR> {

    private final String system;
    private final List<IMqClientInputRListener> listeners;
    private String md5;

    public MqClientPluginR(String system, IMqClientInputRListener... listeners) {
        this.system = system;
        this.listeners = null != listeners ? Arrays.asList(listeners) : Collections.emptyList();
    }

    @Override
    public String onSetupSyncTagToHandle() {
        return BdMqConstants.TAG_C_R;
    }

    @Override
    public Class<MqClientInputR> onGetInputClass() {
        return MqClientInputR.class;
    }

    @Override
    public Class<MqClientOutputR> onGetOutputClass() {
        return MqClientOutputR.class;
    }

    @Override
    public boolean onBeforeSync(String uid, MqClientOutputR output) throws Exception {
        output.setSystem(system);
        output.setMd5(md5);
        return IClientPlugin.super.onBeforeSync(uid, output);
    }

    @Override
    public void onAfterSync(String uid, MqClientInputR input) throws Exception {
        IClientPlugin.super.onAfterSync(uid, input);
        if (!input.isUpdated()) {
            return;
        }
        md5 = input.getMd5();
        listeners.forEach(listener -> listener.onReceiveInputR(input));
    }
}
