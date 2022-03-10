package com.soybeany.sync.server.impl;

import com.soybeany.sync.core.model.BaseSyncerImpl;
import com.soybeany.sync.core.model.SerializeType;
import com.soybeany.sync.core.model.SyncDTO;
import com.soybeany.sync.server.SyncServerService;
import com.soybeany.sync.server.api.IServerPlugin;
import com.soybeany.sync.server.api.IServerSyncer;
import com.soybeany.sync.server.api.ISyncExceptionAware;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Soybeany
 * @date 2021/12/17
 */
@Slf4j
public abstract class BaseServerSyncerImpl extends BaseSyncerImpl<IServerPlugin<?, ?>> implements IServerSyncer, ISyncExceptionAware {

    private SyncServerService service;

    @Override
    public SyncDTO sync(HttpServletRequest request) {
        try {
            Map<String, String> result = service.sync(request);
            return SyncDTO.norm(SerializeType.GSON, result, null);
        } catch (Exception e) {
            return SyncDTO.error(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onStart() {
        super.onStart();
        List<IServerPlugin<?, ?>> plugins = new ArrayList<>();
        onSetupPlugins(plugins);
        service = new SyncServerService(plugins.toArray(new IServerPlugin[0]), this);
    }

    @Override
    public void onSyncException(List<IServerPlugin<Object, Object>> plugins, Exception e) {
        log.warn("同步异常" + getObjNames(plugins), e);
    }
}
