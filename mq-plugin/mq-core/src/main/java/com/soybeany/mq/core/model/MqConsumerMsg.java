package com.soybeany.mq.core.model;

import lombok.Data;

import java.util.List;

/**
 * @author Soybeany
 * @date 2022/1/19
 */
@Data
public class MqConsumerMsg {

    private String stamp;
    private List<String> payloads;

}
