package com.soybeany.mq.core.model;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Soybeany
 * @date 2022/1/19
 */
@Data
public class MqConsumerMsg<T extends Serializable> implements Serializable {

    private Long stamp;
    private final List<T> msgList = new ArrayList<>();

}
