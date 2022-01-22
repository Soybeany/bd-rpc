package com.soybeany.rpc.core.api;

import com.soybeany.sync.core.api.ISystemConfig;

/**
 * @author Soybeany
 * @date 2021/12/17
 */
public interface IRpcClientService extends ISystemConfig {

    String[] onSetupPkgPathToScan();

}
