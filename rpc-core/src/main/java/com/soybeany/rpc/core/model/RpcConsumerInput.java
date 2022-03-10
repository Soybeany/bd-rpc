package com.soybeany.rpc.core.model;

import lombok.Data;

import java.util.Map;
import java.util.Set;

/**
 * @author Soybeany
 * @date 2022/1/17
 */
@Data
public class RpcConsumerInput {

    private boolean updated;
    private String md5;
    private Map<String, Set<RpcServerInfo>> providerMap;

}
