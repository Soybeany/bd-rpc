package com.soybeany.mq.consumer.impl;

import com.soybeany.mq.consumer.api.IMqExceptionHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author Soybeany
 * @date 2022/2/18
 */
@Slf4j
public class MqExceptionHandlerLogImpl implements IMqExceptionHandler {
    @Override
    public boolean onException(String topic, Exception e, long oldStamp, long newStamp, List<?> msgList) {
        log.warn("消息处理异常(" + e.getClass().getSimpleName() + "-" + e.getMessage()
                + ")，topic(" + topic + ")，stamp(" + oldStamp + "->" + newStamp + ")，" + msgList.size() + "条");
        return true;
    }
}
