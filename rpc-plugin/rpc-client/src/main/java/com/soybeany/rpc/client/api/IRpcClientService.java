package com.soybeany.rpc.client.api;

import com.soybeany.sync.client.api.ISystemConfig;

import java.util.Set;

/**
 * @author Soybeany
 * @date 2021/12/17
 */
public interface IRpcClientService extends ISystemConfig {

    void onSetupPkgPathToScan(Set<String> paths);

}
