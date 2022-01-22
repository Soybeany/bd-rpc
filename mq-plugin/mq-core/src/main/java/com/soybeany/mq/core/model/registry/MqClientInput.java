package com.soybeany.mq.core.model.registry;

import lombok.Data;

/**
 * @author Soybeany
 * @date 2022/1/21
 */
@Data
public class MqClientInput {

    private boolean updated;
    private String md5;
    private String[] brokersSyncUrl;

}
