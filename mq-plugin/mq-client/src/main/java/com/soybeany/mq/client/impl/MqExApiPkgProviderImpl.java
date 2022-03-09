package com.soybeany.mq.client.impl;

import com.soybeany.rpc.consumer.api.IRpcExApiPkgProvider;

import java.util.Set;

/**
 * @author Soybeany
 * @date 2022/3/9
 */
class MqExApiPkgProviderImpl implements IRpcExApiPkgProvider {
    @Override
    public void onSetupApiPkgToScan(Set<String> paths) {
        paths.add("com.soybeany.mq.core.api");
    }
}
