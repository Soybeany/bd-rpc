package com.soybeany.mq.core.model.broker;

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
public class MqConsumerOutput extends BaseMqClientOutput {

    /**
     * key为topic，value为stamp
     */
    private final Map<String, Long> topics = new HashMap<>();

}
