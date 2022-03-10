package com.soybeany.mq.producer.api;

import com.soybeany.mq.core.model.MqProducerMsg;

import java.io.Serializable;

/**
 * @author Soybeany
 * @date 2022/1/19
 */
public interface IMqMsgSender {

    <T extends Serializable> void send(String topic, MqProducerMsg<T> msg);

}
