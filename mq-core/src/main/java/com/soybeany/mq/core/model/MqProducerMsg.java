package com.soybeany.mq.core.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @author Soybeany
 * @date 2022/1/20
 */
@Data
public class MqProducerMsg<T extends Serializable> implements Serializable {

    private final long startTime;
    private final long endTime;
    private final T msg;

    public MqProducerMsg(LocalDateTime startTime, LocalDateTime endTime, T msg) {
        if (null == startTime || null == endTime) {
            throw new RuntimeException("MqProducerMsg的startTime或endTime不能为null");
        }
        if (startTime.isAfter(endTime)) {
            throw new RuntimeException("MqProducerMsg的startTime不能晚于endTime");
        }
        this.startTime = toMillis(startTime);
        this.endTime = toMillis(endTime);
        this.msg = msg;
    }

    private long toMillis(LocalDateTime time) {
        return time.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
    }

}
