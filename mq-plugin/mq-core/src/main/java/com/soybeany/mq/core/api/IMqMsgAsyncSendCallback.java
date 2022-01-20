package com.soybeany.mq.core.api;

import com.soybeany.mq.core.exception.MqPluginException;
import com.soybeany.mq.core.model.MqProducerInput;

/**
 * @author Soybeany
 * @date 2022/1/20
 */
public interface IMqMsgAsyncSendCallback {

    void onFinish(MqProducerInput input) throws MqPluginException;

}
