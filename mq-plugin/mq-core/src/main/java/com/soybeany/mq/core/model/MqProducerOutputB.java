package com.soybeany.mq.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Soybeany
 * @date 2022/1/19
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MqProducerOutputB extends BaseMqClientOutputB {

    private final Map<String, List<MqProducerMsgB>> messages = new HashMap<>();

}
