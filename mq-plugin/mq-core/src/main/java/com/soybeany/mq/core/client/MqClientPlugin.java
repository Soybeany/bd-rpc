package com.soybeany.mq.core.client;

import com.soybeany.mq.core.api.IMqClientInputRListener;
import com.soybeany.mq.core.model.BdMqConstants;
import com.soybeany.mq.core.model.registry.MqClientInput;
import com.soybeany.mq.core.model.registry.MqClientOutput;
import com.soybeany.sync.core.api.IClientPlugin;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Soybeany
 * @date 2022/1/21
 */
public class MqClientPlugin implements IClientPlugin<MqClientInput, MqClientOutput> {

    private final String system;
    private final List<IMqClientInputRListener> listeners;
    private String md5;

    public MqClientPlugin(String system, IMqClientInputRListener... listeners) {
        this.system = system;
        this.listeners = null != listeners ? Arrays.asList(listeners) : Collections.emptyList();
    }

    @Override
    public String onSetupSyncTagToHandle() {
        return BdMqConstants.TAG_C_R;
    }

    @Override
    public Class<MqClientInput> onGetInputClass() {
        return MqClientInput.class;
    }

    @Override
    public Class<MqClientOutput> onGetOutputClass() {
        return MqClientOutput.class;
    }

    @Override
    public synchronized boolean onBeforeSync(String uid, MqClientOutput output) throws Exception {
        output.setSystem(system);
        output.setMd5(md5);
        return IClientPlugin.super.onBeforeSync(uid, output);
    }

    @Override
    public synchronized void onAfterSync(String uid, MqClientInput input) throws Exception {
        IClientPlugin.super.onAfterSync(uid, input);
        if (!input.isUpdated()) {
            return;
        }
        md5 = input.getMd5();
        listeners.forEach(listener -> listener.onReceiveInputR(input));
    }
}
