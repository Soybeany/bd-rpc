package com.soybeany.rpc.core.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Soybeany
 * @date 2022/1/17
 */
@Data
public class RpcConsumerInput {

    private final Map<String, Set<ServerInfo>> providerMap = new HashMap<>();

}
