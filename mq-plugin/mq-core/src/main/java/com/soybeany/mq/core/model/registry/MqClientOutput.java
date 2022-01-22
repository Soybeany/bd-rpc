package com.soybeany.mq.core.model.registry;

import com.soybeany.sync.core.model.BaseClientOutput;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Soybeany
 * @date 2022/1/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MqClientOutput extends BaseClientOutput {

    private String md5;

}
