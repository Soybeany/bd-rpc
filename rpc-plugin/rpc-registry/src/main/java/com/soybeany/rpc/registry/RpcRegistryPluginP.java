package com.soybeany.rpc.registry;

import com.soybeany.rpc.core.model.BdRpcConstants;
import com.soybeany.rpc.core.model.RpcProviderInput;
import com.soybeany.rpc.core.model.RpcProviderOutput;

import java.util.Map;

/**
 * 客户端管理插件(P端)
 *
 * @author Soybeany
 * @date 2022/1/17
 */
class RpcRegistryPluginP extends RpcRegistryPlugin<RpcProviderOutput, RpcProviderInput> {

    protected RpcRegistryPluginP(Map<String, IServiceManager> serviceManagerMap) {
        super(serviceManagerMap);
    }

    @Override
    public String onSetupSyncTagToHandle() {
        return BdRpcConstants.TAG_P;
    }

    @Override
    public Class<RpcProviderOutput> onGetInputClass() {
        return RpcProviderOutput.class;
    }

    @Override
    public Class<RpcProviderInput> onGetOutputClass() {
        return RpcProviderInput.class;
    }

    @Override
    protected void onHandleSync(IServiceManager manager, RpcProviderOutput in, RpcProviderInput out) {
        manager.save(in.getServerInfo(), in.getServiceIds());
    }
}
