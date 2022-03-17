package com.soybeany.rpc.consumer.api;

import com.soybeany.cache.v2.contract.IDatasource;
import com.soybeany.cache.v2.contract.IKeyConverter;
import com.soybeany.cache.v2.core.DataManager;
import com.soybeany.cache.v2.log.StdLogger;
import com.soybeany.cache.v2.storage.LruMemCacheStorage;
import com.soybeany.rpc.consumer.anno.BdRpcWired;
import com.soybeany.rpc.consumer.plugin.RpcConsumerPlugin;
import com.soybeany.rpc.core.anno.BdRpcCache;
import com.soybeany.rpc.core.model.RpcServerInfo;
import com.soybeany.sync.client.picker.DataPicker;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Soybeany
 * @date 2022/3/10
 */
public interface IRpcConsumerSyncer {

    int DEFAULT_INVOKE_TIMEOUT = 5;

    static RpcConsumerPlugin getRpcConsumerPlugin(String syncerId, IRpcConsumerSyncer syncer, List<IRpcExApiPkgProvider> apiPkgProviders) {
        Set<String> paths = new HashSet<>();
        syncer.onSetupApiPkgToScan(paths);
        Optional.ofNullable(apiPkgProviders)
                .ifPresent(providers -> providers.forEach(provider -> provider.onSetupApiPkgToScan(paths)));
        return new RpcConsumerPlugin(syncerId, syncer::onGetNewServerPicker, syncer::onSetupInvokeTimeoutSec,
                syncer::onSetupNewDataManager, paths, syncer.onSetupEnableRpcWired());
    }

    /**
     * 配置rpc调用时的超时
     */
    default int onSetupInvokeTimeoutSec(String serviceId) {
        return DEFAULT_INVOKE_TIMEOUT;
    }

    /**
     * 是否允许使用{@link BdRpcWired}注解
     */
    default boolean onSetupEnableRpcWired() {
        return true;
    }

    /**
     * 自定义数据管理器
     */
    default <T> DataManager<T, Object> onSetupNewDataManager(Method method, BdRpcCache cache, IDatasource<T, Object> dataSource, IKeyConverter<T> keyConverter, String desc, String storageId) {
        return DataManager.Builder
                .get(desc, dataSource, keyConverter)
                .logger(cache.needLog() ? new StdLogger<>(IRpcDataManagerProvider.WRITER) : null)
                .withCache(new LruMemCacheStorage.Builder<T, Object>()
                        .capacity(cache.capacity())
                        .enableShareStorage(StringUtils.hasLength(cache.storageId()))
                        .ttl(cache.ttl())
                        .ttlErr(cache.ttlErr())
                        .build()
                )
                .storageId(storageId)
                .enableRenewExpiredCache(cache.enableRenewExpiredCache())
                .build();
    }

    /**
     * 为特定的service配置选择器
     */
    DataPicker<RpcServerInfo> onGetNewServerPicker(String serviceId);

    /**
     * “接口” 所在路径
     */
    void onSetupApiPkgToScan(Set<String> paths);

}
