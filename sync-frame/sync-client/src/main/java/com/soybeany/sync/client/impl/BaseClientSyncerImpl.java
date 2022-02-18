package com.soybeany.sync.client.impl;

import com.soybeany.sync.client.SyncClientService;
import com.soybeany.sync.client.api.IClientPlugin;
import com.soybeany.sync.client.api.ISyncClientConfig;
import com.soybeany.sync.core.model.BaseSyncerImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Soybeany
 * @date 2021/12/16
 */
public abstract class BaseClientSyncerImpl extends BaseSyncerImpl<IClientPlugin<?, ?>> implements ISyncClientConfig {

    protected SyncClientService service;

    @Override
    protected void onStart() {
        super.onStart();
        List<IClientPlugin<?, ?>> plugins = new ArrayList<>();
        onSetupPlugins(plugins);
        service = new SyncClientService(this, toPluginArr(plugins));
        service.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        service.stop();
    }

    @SuppressWarnings("unchecked")
    protected IClientPlugin<Object, Object>[] toPluginArr(List<IClientPlugin<?, ?>> list) {
        return list.toArray(new IClientPlugin[0]);
    }

}
