package com.soybeany.mq.core.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static com.soybeany.sync.core.util.NetUtils.GSON;

/**
 * @author Soybeany
 * @date 2022/1/20
 */
@Data
public class MqProducerMsg {

    private final long startTime;
    private final long endTime;
    private final String msg;

    public static MqProducerMsg createWithJsonMsg(LocalDateTime startTime, LocalDateTime endTime, Object msg) {
        return new MqProducerMsg(startTime, endTime, GSON.toJson(msg));
    }

    public MqProducerMsg(LocalDateTime startTime, LocalDateTime endTime, String msg) {
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
