package com.soybeany.mq.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Soybeany
 * @date 2022/1/19
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MqConsumerOutput extends BaseMqClientOutput {

    private final List<MqTopicInfo> topics = new ArrayList<>();

}
