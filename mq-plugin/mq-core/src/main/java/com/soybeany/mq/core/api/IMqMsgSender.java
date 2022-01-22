package com.soybeany.mq.core.api;

import com.soybeany.mq.core.exception.MqPluginException;
import com.soybeany.mq.core.exception.MqPluginInterruptException;
import com.soybeany.mq.core.model.broker.MqProducerInput;
import com.soybeany.mq.core.model.broker.MqProducerMsg;

import java.util.concurrent.CountDownLatch;

/**
 * @author Soybeany
 * @date 2022/1/19
 */
public interface IMqMsgSender {

    void asyncSend(String topic, MqProducerMsg msg, IMqMsgSendCallback callback);

    default void syncSend(String topic, MqProducerMsg msg) throws MqPluginException {
        // 异步请求
        CountDownLatch latch = new CountDownLatch(1);
        MqProducerInput[] input = new MqProducerInput[1];
        asyncSend(topic, msg, in -> {
            input[0] = in;
            latch.countDown();
        });
        // 同步阻塞等待
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new MqPluginInterruptException("mq消息发送等待中断");
        }
        // 请求结果判断
        if (!input[0].isSuccess()) {
            throw new MqPluginException(input[0].getMsg());
        }
    }

}
