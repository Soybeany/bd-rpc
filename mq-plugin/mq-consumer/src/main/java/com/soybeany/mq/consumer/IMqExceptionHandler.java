package com.soybeany.mq.consumer;

import com.soybeany.mq.core.api.IMqMsgHandler;

/**
 * @author Soybeany
 * @date 2022/1/19
 */
public interface IMqExceptionHandler {

    void onException(Exception e, String payload, IMqMsgHandler handler);

}
