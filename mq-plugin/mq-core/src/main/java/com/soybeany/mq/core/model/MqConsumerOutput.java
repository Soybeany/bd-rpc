package com.soybeany.mq.core.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Soybeany
 * @date 2022/1/19
 */
@Data
public class MqConsumerOutput {

    private final List<MqTopicInfo> topics = new ArrayList<>();

}
