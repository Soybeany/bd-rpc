package com.soybeany.sync.server;

import com.soybeany.sync.core.api.IServerPlugin;
import com.soybeany.sync.core.api.IServiceSyncer;
import com.soybeany.sync.core.exception.SyncException;
import com.soybeany.sync.core.model.BaseServiceImpl;
import com.soybeany.sync.core.model.SyncDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Soybeany
 * @date 2021/12/17
 */
public abstract class BaseServerServiceImpl extends BaseServiceImpl<IServerPlugin> implements IServiceSyncer {

    private SyncServerService service;

    @Override
    public SyncDTO sync(HttpServletRequest request) {
        try {
            Map<String, String> result = service.sync(request);
            return SyncDTO.norm(result);
        } catch (SyncException e) {
            return SyncDTO.error(e);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        List<IServerPlugin> plugins = new ArrayList<>();
        onSetupPlugins(plugins);
        service = new SyncServerService(plugins.toArray(new IServerPlugin[0]));
    }

}
