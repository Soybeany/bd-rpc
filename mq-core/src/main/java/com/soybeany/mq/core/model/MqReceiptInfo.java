package com.soybeany.mq.core.model;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Soybeany
 * @date 2022/2/22
 */
@Data
public class MqReceiptInfo implements Serializable {

    private final String topic;
    private final Long oldStamp;
    private final Long newStamp;

    @ToString(callSuper = true)
    public static class WithE extends MqReceiptInfo {

        @Getter
        private final Exception e;

        public WithE(String topic, Long oldStamp, Long newStamp, Exception e) {
            super(topic, oldStamp, newStamp);
            this.e = e;
        }
    }

}
