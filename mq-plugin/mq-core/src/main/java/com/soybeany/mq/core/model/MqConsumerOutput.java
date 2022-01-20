package com.soybeany.mq.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Soybeany
 * @date 2022/1/19
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MqConsumerOutput extends BaseMqClientOutput {

    private final Map<String, String> actStamps = new HashMap<>();
    private Set<String> topics;

}
