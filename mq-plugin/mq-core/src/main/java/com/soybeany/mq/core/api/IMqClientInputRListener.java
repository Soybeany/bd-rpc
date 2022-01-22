package com.soybeany.mq.core.api;

import com.soybeany.mq.core.model.registry.MqClientInput;

/**
 * @author Soybeany
 * @date 2022/1/22
 */
public interface IMqClientInputRListener {

    void onReceiveInputR(MqClientInput input);

}
