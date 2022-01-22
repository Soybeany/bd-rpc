package com.soybeany.rpc.registry;

import com.soybeany.rpc.core.model.BdRpcConstants;
import com.soybeany.rpc.core.model.RpcConsumerInput;
import com.soybeany.rpc.core.model.RpcConsumerOutput;
import com.soybeany.rpc.core.model.ServerInfo;
import com.soybeany.util.Md5Utils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static com.soybeany.sync.core.util.RequestUtils.GSON;

/**
 * 客户端管理插件(C端)
 *
 * @author Soybeany
 * @date 2022/1/17
 */
class RpcRegistryPluginC extends RpcRegistryPlugin<RpcConsumerOutput, RpcConsumerInput> {

    protected RpcRegistryPluginC(Map<String, IStorageManager> storageManagerMap) {
        super(storageManagerMap);
    }

    @Override
    public String onSetupSyncTagToHandle() {
        return BdRpcConstants.TAG_C;
    }

    @Override
    public Class<RpcConsumerOutput> onGetInputClass() {
        return RpcConsumerOutput.class;
    }

    @Override
    public Class<RpcConsumerInput> onGetOutputClass() {
        return RpcConsumerInput.class;
    }

    @Override
    protected void onHandleSync(IStorageManager manager, RpcConsumerOutput in, RpcConsumerInput out) {
        Map<String, Set<ServerInfo>> map = new LinkedHashMap<>();
        in.getServiceIds().forEach(id -> map.put(id, manager.load(id)));
        // 当md5不一致时，再返回数据
        String md5 = Md5Utils.strToMd5(GSON.toJson(map));
        if (!md5.equals(in.getMd5())) {
            out.setUpdated(true);
            out.setMd5(md5);
            out.setProviderMap(map);
        }
    }
}
