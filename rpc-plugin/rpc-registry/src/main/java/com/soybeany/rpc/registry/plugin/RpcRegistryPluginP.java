package com.soybeany.rpc.registry.plugin;

import com.soybeany.rpc.core.model.BdRpcConstants;
import com.soybeany.rpc.core.model.RpcProviderInput;
import com.soybeany.rpc.core.model.RpcProviderOutput;
import com.soybeany.rpc.registry.api.IRpcStorageManager;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * 客户端管理插件(P端)
 *
 * @author Soybeany
 * @date 2022/1/17
 */
@RequiredArgsConstructor
class RpcRegistryPluginP extends RpcRegistryPlugin<RpcProviderOutput, RpcProviderInput> {

    private final Map<String, RpcProviderOutput> cacheMap = new WeakHashMap<>();
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
        RpcProviderOutput cache = cacheMap.get(in.getProviderId());
        // 数据更新了，更新缓存
        if (in.isUpdated()) {
            cacheMap.put(in.getProviderId(), cache = in);
        }
        // 缓存为空，则直接返回
        if (null == cache) {
            out.setMd5(null);
            return;
        }
        // 更新存储管理器数据
        storageManager.save(cache.getSystem(), cache.getRpcServerInfo(), cache.getServiceIds());
        out.setMd5(cache.getMd5());
    }

}
