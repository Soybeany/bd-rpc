package com.soybeany.rpc.core.model;

import com.soybeany.sync.core.api.ISyncClientConfig;

/**
 * @author Soybeany
 * @date 2021/12/16
 */
public abstract class BaseClientServiceImpl extends BaseServiceImpl implements ISyncClientConfig {

    protected abstract String onSetupSystem();

    protected abstract String[] onSetupPkgPathToScan();

}
