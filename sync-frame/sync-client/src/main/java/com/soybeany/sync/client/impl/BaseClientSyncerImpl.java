package com.soybeany.sync.client.impl;

import com.soybeany.sync.client.SyncClientService;
import com.soybeany.sync.client.api.IClientPlugin;
import com.soybeany.sync.client.api.ISyncClientConfig;
import com.soybeany.sync.client.api.ISyncExceptionWatcher;
import com.soybeany.sync.client.model.SyncState;
import com.soybeany.sync.core.model.BaseSyncerImpl;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Soybeany
 * @date 2021/12/16
 */
@Slf4j
public abstract class BaseClientSyncerImpl extends BaseSyncerImpl<IClientPlugin<?, ?>> implements ISyncClientConfig, ISyncExceptionWatcher {

    protected SyncClientService service;

    @Override
    protected void onStart() {
        super.onStart();
        List<IClientPlugin<?, ?>> plugins = new ArrayList<>();
        onSetupPlugins(plugins);
        service = new SyncClientService(this, toPluginArr(plugins), this);
        service.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        service.stop();
    }

    @Override
    public void onSyncException(List<IClientPlugin<Object, Object>> plugins, String uid, SyncState state, Exception e) {
        log.warn("同步异常" + getObjNames(plugins) + "(" + state + "): " + e.getMessage());
    }

    @SuppressWarnings("unchecked")
    protected IClientPlugin<Object, Object>[] toPluginArr(List<IClientPlugin<?, ?>> list) {
        return list.toArray(new IClientPlugin[0]);
    }

}
