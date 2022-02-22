package com.soybeany.mq.core.model;

import lombok.Getter;

/**
 * @author Soybeany
 * @date 2022/2/22
 */
public class MqTopicExceptionInfo extends MqTopicInfo {

    @Getter
    private final Exception e;

    public MqTopicExceptionInfo(String topic, Long stamp, Exception e) {
        super(topic, stamp);
        this.e = e;
    }

}
