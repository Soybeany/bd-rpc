package com.soybeany.mq.client.plugin;

import com.soybeany.rpc.consumer.api.IRpcServiceProxyAware;
import com.soybeany.sync.client.api.IClientPlugin;

/**
 * @author Soybeany
 * @date 2022/1/29
 */
public abstract class BaseMqClientRegistryPlugin implements IClientPlugin<Object, Object>, IRpcServiceProxyAware {
    @Override
    public String onSetupSyncTagToHandle() {
        return null;
    }

    @Override
    public Class<Object> onGetInputClass() {
        return Object.class;
    }

    @Override
    public Class<Object> onGetOutputClass() {
        return Object.class;
    }

    @Override
    public boolean onBeforeSync(String uid, Object o) {
        return false;
    }
}
