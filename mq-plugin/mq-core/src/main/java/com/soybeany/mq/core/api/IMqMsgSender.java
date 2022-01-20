package com.soybeany.mq.core.api;

import com.soybeany.mq.core.exception.MqPluginException;
import com.soybeany.mq.core.model.MqProducerMsg;

/**
 * @author Soybeany
 * @date 2022/1/19
 */
public interface IMqMsgSender {

    void syncSend(MqProducerMsg msg) throws MqPluginException;

    void asyncSend(MqProducerMsg msg, IMqMsgAsyncSendCallback callback);

}
