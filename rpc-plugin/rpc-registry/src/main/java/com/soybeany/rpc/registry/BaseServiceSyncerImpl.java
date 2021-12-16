package com.soybeany.rpc.registry;

import com.soybeany.rpc.core.api.ServiceSyncer;
import com.soybeany.rpc.core.model.BaseServiceImpl;
import com.soybeany.sync.core.exception.SyncException;
import com.soybeany.sync.core.model.SyncDTO;
import com.soybeany.sync.server.SyncServerService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Soybeany
 * @date 2021/12/16
 */
public abstract class BaseServiceSyncerImpl extends BaseServiceImpl implements ServiceSyncer {

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
        RpcRegistryPlugin plugin = new RpcRegistryPlugin(onSetupAcceptableSystems(), this::onGetNewServiceManager);
        service = new SyncServerService(plugin);
    }

    // ***********************子类实现****************************

    protected abstract String[] onSetupAcceptableSystems();

    protected abstract IServiceManager onGetNewServiceManager(String system);

}
