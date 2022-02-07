package com.soybeany.rpc.core.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Soybeany
 * @date 2022/2/7
 */
@Data
@Accessors(fluent = true, chain = true)
public class RpcBatchConfig {

    private String tag;

}
