package com.soybeany.rpc.registry;

import com.google.gson.reflect.TypeToken;
import com.soybeany.rpc.core.exception.RpcPluginException;
import com.soybeany.rpc.core.model.ServerInfo;
import com.soybeany.sync.core.api.IServerPlugin;
import com.soybeany.sync.core.exception.SyncException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.soybeany.rpc.core.model.BdRpcConstants.*;
import static com.soybeany.sync.core.util.RequestUtils.GSON;

/**
 * 客户端管理插件
 *
 * @author Soybeany
 * @date 2021/10/26
 */
@Component
public abstract class BaseRpcRegistryPlugin implements IServerPlugin {

    private static final Type TYPE_ID_SET = new TypeToken<Set<String>>() {
    }.getType();

    private final Map<String, IServiceManager> serviceManagerMap = getServiceManagerMap();

    @Override
    public void onHandleSync(Map<String, String> param, Map<String, String> result) throws SyncException {
        IServiceManager serviceManager = serviceManagerMap.get(param.get(KEY_SYSTEM));
        if (null == serviceManager) {
            throw new SyncException("非注册系统，不允许同步");
        }
        switch (param.get(KEY_ACTION)) {
            case ACTION_GET_PROVIDERS:
                Map<String, Set<ServerInfo>> map = new HashMap<>();
                for (String id : GSON.fromJson(param.get(KEY_SERVICE_ID_ARR), String[].class)) {
                    map.put(id, serviceManager.load(id));
                }
                result.put(KEY_PROVIDER_MAP, GSON.toJson(map));
                break;
            case ACTION_REGISTER_PROVIDERS:
                ServerInfo info = GSON.fromJson(param.get(KEY_PROVIDER_INFO), ServerInfo.class);
                Set<String> serviceIds = GSON.fromJson(param.get(KEY_SERVICE_ID_ARR), TYPE_ID_SET);
                serviceManager.save(info, serviceIds);
                break;
            default:
                throw new RpcPluginException("暂不支持此操作");
        }
    }

    @Override
    public String onSetupSyncTagToHandle() {
        return TAG;
    }

    // ***********************内部方法****************************

    private Map<String, IServiceManager> getServiceManagerMap() {
        Map<String, IServiceManager> map = new HashMap<>();
        for (String system : onSetupAcceptableSystems()) {
            map.put(system, onGetNewServiceManager(system));
        }
        return map;
    }

    // ***********************子类实现****************************

    /**
     * 配置可接受的系统
     *
     * @return 系统名称数组
     */
    @NonNull
    protected abstract String[] onSetupAcceptableSystems();

    /**
     * 获取指定系统的服务管理器
     *
     * @param system 系统名称
     * @return 提供的管理器
     */
    @NonNull
    protected abstract IServiceManager onGetNewServiceManager(String system);

}
