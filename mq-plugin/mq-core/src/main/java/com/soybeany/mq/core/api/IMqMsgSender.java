package com.soybeany.mq.core.api;

import com.soybeany.mq.core.model.MqProducerMsg;

/**
 * @author Soybeany
 * @date 2022/1/19
 */
public interface IMqMsgSender {

    void send(String topic, MqProducerMsg msg, IMqMsgSendCallback callback);

}
