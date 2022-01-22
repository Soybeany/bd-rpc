package com.soybeany.mq.core.api;

import com.soybeany.mq.core.model.MqClientInputR;

/**
 * @author Soybeany
 * @date 2022/1/22
 */
public interface IMqClientInputRListener {

    void onReceiveInputR(MqClientInputR input);

}
