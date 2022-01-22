package com.soybeany.mq.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Soybeany
 * @date 2022/1/19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MqProducerInputB {

    private boolean success;
    private String msg;

}
