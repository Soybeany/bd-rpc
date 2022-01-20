package com.soybeany.mq.core.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Soybeany
 * @date 2022/1/19
 */
@Data
public class MqConsumerMsg {

    private Long stamp;
    private final List<String> messages = new ArrayList<>();

}
