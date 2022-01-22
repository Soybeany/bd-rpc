package com.soybeany.mq.core.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Soybeany
 * @date 2022/1/19
 */
@Data
public class MqConsumerInputB {

    private final Map<String, MqConsumerMsgB> messages = new HashMap<>();

}
