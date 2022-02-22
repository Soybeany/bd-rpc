package com.soybeany.mq.consumer.api;

import java.io.Serializable;
import java.util.List;

/**
 * @author Soybeany
 * @date 2022/1/19
 */
public interface IMqMsgHandler<T extends Serializable> {

    String onSetupTopic();

    void onHandle(List<T> msgList);

}
