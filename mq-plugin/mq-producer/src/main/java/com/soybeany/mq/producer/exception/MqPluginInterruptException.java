package com.soybeany.mq.producer.exception;

import com.soybeany.mq.core.exception.MqPluginException;

/**
 * @author Soybeany
 * @date 2022/1/20
 */
public class MqPluginInterruptException extends MqPluginException {
    public MqPluginInterruptException(String msg) {
        super(msg);
    }
}
