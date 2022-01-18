package com.soybeany.sync.client;

import com.soybeany.sync.core.api.IClientPlugin;
import com.soybeany.sync.core.api.ISyncClientConfig;
import com.soybeany.sync.core.model.BaseServiceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Soybeany
 * @date 2021/12/16
 */
public abstract class BaseClientServiceImpl extends BaseServiceImpl<IClientPlugin<?, ?>> implements ISyncClientConfig {

    private SyncClientService service;

    @SuppressWarnings("unchecked")
    @Override
    protected void onStart() {
        super.onStart();
        List<IClientPlugin<?, ?>> plugins = new ArrayList<>();
        onSetupPlugins(plugins);
        service = new SyncClientService(this, plugins.toArray(new IClientPlugin[0]));
        service.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        service.stop();
    }

}
