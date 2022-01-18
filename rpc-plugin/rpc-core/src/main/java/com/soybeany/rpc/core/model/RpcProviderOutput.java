package com.soybeany.rpc.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

/**
 * @author Soybeany
 * @date 2022/1/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RpcProviderOutput extends BaseRpcClientOutput {

    private ServerInfo serverInfo;
    private Set<String> serviceIds;

}
