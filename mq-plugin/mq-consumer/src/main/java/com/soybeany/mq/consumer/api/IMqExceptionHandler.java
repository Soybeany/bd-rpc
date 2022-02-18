package com.soybeany.mq.consumer.api;

/**
 * @author Soybeany
 * @date 2022/1/19
 */
public interface IMqExceptionHandler {

    void onException(Exception e, String payload, IMqMsgHandler handler);

}
