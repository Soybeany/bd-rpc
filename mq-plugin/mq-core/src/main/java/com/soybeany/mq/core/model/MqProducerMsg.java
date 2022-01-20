package com.soybeany.mq.core.model;

import lombok.Data;

import java.time.LocalDateTime;

import static com.soybeany.sync.core.util.RequestUtils.GSON;

/**
 * @author Soybeany
 * @date 2022/1/20
 */
@Data
public class MqProducerMsg {

    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final String msg;

    public static MqProducerMsg createWithJsonMsg(LocalDateTime startTime, LocalDateTime endTime, Object msg) {
        return new MqProducerMsg(startTime, endTime, GSON.toJson(msg));
    }

}
