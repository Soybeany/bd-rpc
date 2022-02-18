package com.soybeany.mq.consumer;

import com.soybeany.mq.core.api.IMqMsgHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Soybeany
 * @date 2022/2/18
 */
@Slf4j
public class MqExceptionHandlerLogImpl implements IMqExceptionHandler {
    @Override
    public void onException(Exception e, String payload, IMqMsgHandler handler) {
        log.error("“" + handler.getClass() + "”处理“" + payload + "”异常(" + e.getMessage() + ")");
    }
}
