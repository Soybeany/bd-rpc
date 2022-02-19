package com.soybeany.mq.consumer.impl;

import com.soybeany.mq.consumer.api.IMqExceptionHandler;
import com.soybeany.mq.consumer.api.IMqMsgHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author Soybeany
 * @date 2022/2/18
 */
@Slf4j
public class MqExceptionHandlerLogImpl implements IMqExceptionHandler {
    @Override
    public boolean onException(Exception e, List<String> payloads, IMqMsgHandler handler) {
        log.error("“" + handler.getClass() + "”处理“" + payloads + "”异常(" + e.getMessage() + ")");
        return false;
    }
}
