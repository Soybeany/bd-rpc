package com.soybeany.rpc.core.model;

import com.soybeany.sync.core.model.BaseClientOutput;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

/**
 * @author Soybeany
 * @date 2022/1/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RpcProviderOutput extends BaseClientOutput {

    private boolean updated;
    private String providerId;
    private String md5;
    private ServerInfo serverInfo;
    private Set<String> serviceIds;

}
