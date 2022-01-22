package com.soybeany.mq.core.api;

import com.soybeany.mq.core.exception.MqPluginException;
import com.soybeany.mq.core.model.MqProducerInputB;

/**
 * @author Soybeany
 * @date 2022/1/20
 */
public interface IMqMsgSendCallback {

    void onFinish(MqProducerInputB input) throws MqPluginException;

}
