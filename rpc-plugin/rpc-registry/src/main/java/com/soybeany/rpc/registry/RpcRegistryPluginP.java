package com.soybeany.rpc.registry;

import com.soybeany.rpc.core.model.BdRpcConstants;
import com.soybeany.rpc.core.model.RpcProviderInput;
import com.soybeany.rpc.core.model.RpcProviderOutput;
import lombok.RequiredArgsConstructor;

/**
 * 客户端管理插件(P端)
 *
 * @author Soybeany
 * @date 2022/1/17
 */
@RequiredArgsConstructor
class RpcRegistryPluginP extends RpcRegistryPlugin<RpcProviderOutput, RpcProviderInput> {

    private final IRpcStorageManager storageManager;

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
    public void onHandleSync(RpcProviderOutput in, RpcProviderInput out) {
        storageManager.save(in.getSystem(), in.getServerInfo(), in.getServiceIds());
    }
}
