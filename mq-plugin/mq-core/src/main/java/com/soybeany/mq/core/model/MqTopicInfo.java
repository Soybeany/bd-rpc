package com.soybeany.mq.core.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Soybeany
 * @date 2022/2/18
 */
@Data
public class MqTopicInfo implements Serializable {
    /**
     * mq主题
     */
    private final String topic;

    /**
     * 上一次同步至的戳
     */
    private final Long stamp;
}
