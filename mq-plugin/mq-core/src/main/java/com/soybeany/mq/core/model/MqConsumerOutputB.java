package com.soybeany.mq.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Soybeany
 * @date 2022/1/19
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MqConsumerOutputB extends BaseMqClientOutputB {

    /**
     * key为topic，value为stamp
     */
    private final Map<String, Long> topics = new HashMap<>();

}
