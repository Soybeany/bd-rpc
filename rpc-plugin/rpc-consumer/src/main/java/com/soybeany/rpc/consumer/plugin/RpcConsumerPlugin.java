package com.soybeany.rpc.consumer.plugin;

import com.soybeany.cache.v2.contract.IDatasource;
import com.soybeany.cache.v2.core.DataManager;
import com.soybeany.cache.v2.log.ILogWriter;
import com.soybeany.cache.v2.log.StdLogger;
import com.soybeany.cache.v2.storage.LruMemCacheStorage;
import com.soybeany.rpc.client.anno.BdRpc;
import com.soybeany.rpc.client.model.RpcMethodInfo;
import com.soybeany.rpc.client.plugin.BaseRpcClientPlugin;
import com.soybeany.rpc.consumer.anno.BdRpcBatch;
import com.soybeany.rpc.consumer.anno.BdRpcCache;
import com.soybeany.rpc.consumer.api.IRpcBatchInvoker;
import com.soybeany.rpc.consumer.api.IRpcServiceProxy;
import com.soybeany.rpc.consumer.api.IServerInfoReceiver;
import com.soybeany.rpc.consumer.exception.RpcPluginNoFallbackException;
import com.soybeany.rpc.consumer.exception.RpcRequestException;
import com.soybeany.rpc.consumer.model.RpcBatchResult;
import com.soybeany.rpc.consumer.model.RpcProxySelector;
import com.soybeany.rpc.core.exception.RpcPluginException;
import com.soybeany.rpc.core.model.RpcConsumerInput;
import com.soybeany.rpc.core.model.RpcConsumerOutput;
import com.soybeany.rpc.core.model.RpcServerInfo;
import com.soybeany.sync.core.exception.SyncRequestException;
import com.soybeany.sync.core.model.SyncClientInfo;
import com.soybeany.sync.core.model.SyncDTO;
import com.soybeany.sync.core.picker.DataPicker;
import com.soybeany.sync.core.util.RequestUtils;
import com.soybeany.util.Md5Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;

import static com.soybeany.rpc.core.model.BdRpcConstants.*;
import static com.soybeany.sync.core.util.RequestUtils.GSON;

/**
 * // todo 引入调用失败告警配置，方法级别注解(绑定alertId)，触发后自动找相应alertId的处理器（支持配置不同的阈值策略，达到才真正告警），并将产生告警的数据传入
 *
 * @author Soybeany
 * @date 2021/10/27
 */
@RequiredArgsConstructor
public class RpcConsumerPlugin extends BaseRpcClientPlugin<RpcConsumerInput, RpcConsumerOutput> implements IRpcServiceProxy {

    private static final String RESOURCE_PATTERN = "/**/*.class";
    private static final String GROUP_SEPARATOR = ":";

    /**
     * 已加载的代理（本地/远程）
     */
    private final Map<Class<?>, Object> proxies = new HashMap<>();

    /**
     * 服务器信息提供者的映射
     */
    private final Map<String, DataPicker<RpcServerInfo>> pickers = new HashMap<>();
    private final Map<Method, DataManager<InvokeInfo, Object>> dataManagerMap = new HashMap<>();
    private final Map<Class<?>, Map<String, InfoPart2>> infoPart2Map = new HashMap<>();
    private final Set<String> serviceIdSet = new HashSet<>();

    private final String system;
    private final String version;
    private final ApplicationContext appContext;
    private final Function<String, DataPicker<RpcServerInfo>> dataPickerProvider;
    private final Function<String, Integer> timeoutInSecProvider;
    private final Set<String> pkgToScan;

    private ExecutorService batchExecutor;
    private String md5;

    @Override
    public String onSetupSyncTagToHandle() {
        return TAG_C;
    }

    @Override
    public Class<RpcConsumerInput> onGetInputClass() {
        return RpcConsumerInput.class;
    }

    @Override
    public Class<RpcConsumerOutput> onGetOutputClass() {
        return RpcConsumerOutput.class;
    }

    @SuppressWarnings("AlibabaThreadPoolCreation")
    @Override
    public void onStartup(SyncClientInfo info) {
        super.onStartup(info);
        //spring工具类，可以获取指定路径下的全部类
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        try {
            Set<Resource> resources = new HashSet<>();
            for (String path : getPostTreatPkgPathsToScan()) {
                String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath(path) + RESOURCE_PATTERN;
                Resource[] partResources = resourcePatternResolver.getResources(pattern);
                Collections.addAll(resources, partResources);
            }
            //MetadataReader 的工厂类
            MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
            for (Resource resource : resources) {
                //用于读取类信息
                MetadataReader reader = readerFactory.getMetadataReader(resource);
                //扫描到的class
                String classname = reader.getClassMetadata().getClassName();
                Class<?> clazz = Class.forName(classname);
                //处理指定的注解
                Optional.ofNullable(clazz.getAnnotation(BdRpc.class))
                        .filter(bdRpc -> clazz.isInterface())
                        .ifPresent(bdRpc -> setupServiceImpl(clazz, getId(version, bdRpc), bdRpc.timeoutInSec()));
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RpcPluginException("路径元信息解析异常:" + e.getMessage());
        }
        // 按需启动线程池
        if (!infoPart2Map.isEmpty()) {
            batchExecutor = Executors.newCachedThreadPool();
        }
    }

    @Override
    public void onShutdown() {
        if (null != batchExecutor) {
            batchExecutor.shutdown();
        }
        super.onShutdown();
    }

    @Override
    public synchronized boolean onBeforeSync(String uid, RpcConsumerOutput output) throws Exception {
        output.setSystem(system);
        output.setServiceIds(serviceIdSet);
        output.setMd5(md5);
        return super.onBeforeSync(uid, output);
    }

    @Override
    public synchronized void onAfterSync(String uid, RpcConsumerInput input) throws Exception {
        super.onAfterSync(uid, input);
        if (!input.isUpdated()) {
            return;
        }
        Set<String> keys = new HashSet<>(pickers.keySet());
        Optional.ofNullable(input.getProviderMap()).ifPresent(map -> {
            // 数据预处理，添加带标签的记录
            Map<String, Set<RpcServerInfo>> tmpMap = new HashMap<>();
            map.forEach((id, v) -> v.forEach(info -> {
                String newId = getKeyWithGroup(info.getGroup(), id);
                if (!newId.equals(id)) {
                    tmpMap.computeIfAbsent(newId, s -> new HashSet<>()).add(info);
                }
            }));
            map.putAll(tmpMap);
            // 更新数据
            map.forEach((id, v) -> {
                keys.remove(id);
                DataPicker<RpcServerInfo> picker = pickers.computeIfAbsent(id, dataPickerProvider);
                picker.set(new ArrayList<>(v));
            });
        });
        // 移除已失效条目
        keys.forEach(pickers::remove);
        // 信息处理成功，最后再更新md5
        md5 = input.getMd5();
    }

    @SuppressWarnings("unchecked")
    @Override
    public synchronized <T> T get(Class<T> interfaceClass) {
        // 从代理实现中找
        T instance = (T) proxies.get(interfaceClass);
        if (null != instance) {
            return instance;
        }
        // 从spring容器中找
        T impl = getBeanFromContext(interfaceClass);
        if (null != impl) {
            return impl;
        }
        // 没有找到实现类
        throw getNotFoundClassImplException(interfaceClass);
    }

    @Override
    public <T> IRpcBatchInvoker<T> getBatch(Class<?> interfaceClass, String methodId) {
        // 入参校验
        Map<String, InfoPart2> map = infoPart2Map.get(interfaceClass);
        if (null == map) {
            throw getNotFoundClassImplException(interfaceClass);
        }
        InfoPart2 infoPart = map.get(methodId);
        if (null == infoPart) {
            throw new RpcPluginException("没有找到指定methodId(" + methodId + ")的方法");
        }
        return args -> {
            // 并发请求
            Map<RpcServerInfo, Future<T>> futureMap = new HashMap<>();
            InvokeInfo info = infoPart.toNewInfo(args);
            DataPicker<RpcServerInfo> picker = getGroupedPicker(info);
            for (RpcServerInfo serverInfo : picker.getAllUsable()) {
                Future<T> future = batchExecutor.submit(() -> onInvoke(new PickerWrapper(picker, serverInfo), info));
                futureMap.put(serverInfo, future);
            }
            // 结果整合
            Map<RpcServerInfo, RpcBatchResult<T>> result = new HashMap<>();
            futureMap.forEach((serverInfo, future) -> {
                try {
                    result.put(serverInfo, RpcBatchResult.norm(future.get()));
                } catch (InterruptedException e) {
                    result.put(serverInfo, RpcBatchResult.error(e));
                } catch (ExecutionException e) {
                    result.put(serverInfo, RpcBatchResult.error(e.getCause()));
                }
            });
            return result;
        };
    }

    @Override
    protected Set<String> onSetupPkgPathToScan() {
        return pkgToScan;
    }

    // ***********************内部方法****************************

    @SuppressWarnings("unchecked")
    private <T> T invoke(InvokeInfo invokeInfo) throws Throwable {
        DataManager<InvokeInfo, Object> manager = dataManagerMap.get(invokeInfo.method);
        // 若没有对应的manager，即不需缓存，直接调用服务提供者
        if (null == manager) {
            return onInvoke(invokeInfo);
        }
        // 需要则使用缓存
        return (T) manager.getData(invokeInfo);
    }

    private DataManager<InvokeInfo, Object> getNewDataManager(BdRpcCache cache) {
        boolean hasStorageId = StringUtils.hasLength(cache.storageId());
        String storageId = hasStorageId ? cache.storageId() : cache.desc();
        return DataManager.Builder
                .get(cache.desc(), (IDatasource<InvokeInfo, Object>) this::onInvoke, invokeInfo -> {
                    String keyWithGroup = getKeyWithGroup(invokeInfo.group, GSON.toJson(invokeInfo.args));
                    return cache.useMd5Key() ? Md5Utils.strToMd5(keyWithGroup) : keyWithGroup;
                })
                .logger(cache.needLog() ? new StdLogger<>(new CacheLogWriter()) : null)
                .withCache(new LruMemCacheStorage.Builder<InvokeInfo, Object>()
                        .capacity(cache.capacity())
                        .enableShareStorage(hasStorageId)
                        .ttl(cache.ttl())
                        .ttlErr(cache.ttlErr())
                        .build()
                )
                .storageId(storageId)
                .enableRenewExpiredCache(cache.enableRenewExpiredCache())
                .build();
    }

    private DataPicker<RpcServerInfo> getGroupedPicker(InvokeInfo invokeInfo) {
        String serviceIdWithGroup = getKeyWithGroup(invokeInfo.group, invokeInfo.serviceId);
        return Optional.ofNullable(pickers.get(serviceIdWithGroup)).orElseThrow(() -> new RpcPluginException("暂无serviceId(" + serviceIdWithGroup + ")可用的服务提供者"));
    }

    private <T> T onInvoke(InvokeInfo invokeInfo) throws Exception {
        return onInvoke(getGroupedPicker(invokeInfo), invokeInfo);
    }

    private <T> T onInvoke(DataPicker<RpcServerInfo> picker, InvokeInfo invokeInfo) throws Exception {
        if (null == picker) {
            return invokeMethodOfFallbackImpl(invokeInfo.method, invokeInfo.args, invokeInfo.fallbackImpl, "暂无serviceId(" + invokeInfo.serviceId + ")的服务提供者信息");
        }
        RequestUtils.Config config = new RequestUtils.Config();
        config.getParams().put(KEY_METHOD_INFO, GSON.toJson(new RpcMethodInfo(invokeInfo.serviceId, invokeInfo.method, invokeInfo.args)));
        config.setTimeoutInSec(invokeInfo.timeoutInSec);
        RequestUtils.Result<RpcServerInfo, SyncDTO> result;
        try {
            result = RequestUtils.request(picker, serverInfo -> {
                config.getHeaders().put(HEADER_AUTHORIZATION, serverInfo.getAuthorization());
                return serverInfo.getInvokeUrl();
            }, config, SyncDTO.class, "暂无serviceId(" + invokeInfo.serviceId + ")可用的服务提供者");
        } catch (SyncRequestException e) {
            return invokeMethodOfFallbackImpl(invokeInfo.method, invokeInfo.args, invokeInfo.fallbackImpl, e.getMessage());
        }
        SyncDTO dto = result.getData();
        // 正常则直接返回结果
        if (dto.getIsNorm()) {
            return dto.getData(invokeInfo.method.getGenericReturnType());
        }
        // 非正常时按情况抛出异常
        Exception exception = dto.getParsedErr();
        if (exception instanceof SyncRequestException) {
            exception = new RpcRequestException(result.getUrl(), (SyncRequestException) exception);
        }
        if (exception instanceof IServerInfoReceiver) {
            ((IServerInfoReceiver) exception).onSetupServerInfo(result.getUrl());
        }
        throw exception;
    }

    @SuppressWarnings("unchecked")
    private <T> T invokeMethodOfFallbackImpl(Method method, Object[] args, Object fallbackImpl, String errMsg) throws Exception {
        if (null == fallbackImpl) {
            throw new RpcPluginNoFallbackException(errMsg);
        } else {
            return (T) method.invoke(fallbackImpl, args);
        }
    }

    private void setupServiceImpl(Class<?> interfaceClass, String serviceId, int referTimeoutInSec) {
        // 记录serviceId
        boolean success = serviceIdSet.add(serviceId);
        if (!success) {
            throw new RpcPluginException("@BdRpc的serviceId(" + serviceId + ")需唯一");
        }
        Object fallbackImpl = null;
        // 配置本地熔断实现
        Object impl = getBeanFromContext(interfaceClass);
        if (null != impl && isFallbackImpl(impl)) {
            fallbackImpl = impl;
        }
        // 分区信息生成
        int timeoutInSec = (referTimeoutInSec >= 0 ? referTimeoutInSec : timeoutInSecProvider.apply(serviceId));
        InfoPart1 infoPart = new InfoPart1(fallbackImpl, serviceId, timeoutInSec);
        // 方法信息预处理
        for (Method method : interfaceClass.getMethods()) {
            handleMethod(interfaceClass, method, infoPart);
        }
        // 远端服务
        Object instance = Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class[]{interfaceClass},
                (proxy, method, args) -> invoke(infoPart.toNewInfo(method, args))
        );
        proxies.put(interfaceClass, instance);
    }

    private void handleMethod(Class<?> interfaceClass, Method method, InfoPart1 part1) {
        // 处理缓存注解
        BdRpcCache cache = method.getAnnotation(BdRpcCache.class);
        if (null != cache) {
            dataManagerMap.put(method, getNewDataManager(cache));
        }
        // 处理批量注解
        BdRpcBatch batch = method.getAnnotation(BdRpcBatch.class);
        if (null != batch) {
            InfoPart2 previous = infoPart2Map.computeIfAbsent(interfaceClass, clazz -> new HashMap<>())
                    .put(batch.methodId(), new InfoPart2(method, part1));
            if (null != previous) {
                throw new RpcPluginException("同一个类中，@BdRpcBatch的methodId(" + batch.methodId() + ")需唯一");
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T getBeanFromContext(Class<?> interfaceClass) {
        try {
            return (T) appContext.getBean(interfaceClass);
        } catch (NoSuchBeanDefinitionException ignore) {
            return null;
        }
    }

    private String getKeyWithGroup(String group, String key) {
        return StringUtils.hasText(group) ? (group + GROUP_SEPARATOR + key) : key;
    }

    private RpcPluginException getNotFoundClassImplException(Class<?> interfaceClass) {
        return new RpcPluginException("没有找到指定类(" + interfaceClass + ")的实现");
    }

    // ***********************内部类****************************

    @RequiredArgsConstructor
    private static class InfoPart1 {
        final Object fallbackImpl;
        final String serviceId;
        final int timeoutInSec;

        InvokeInfo toNewInfo(Method method, Object[] args) {
            return new InvokeInfo(method, args, fallbackImpl, serviceId, timeoutInSec);
        }
    }

    private static class InfoPart2 {
        final Method method;
        final Object fallbackImpl;
        final String serviceId;
        final int timeoutInSec;

        InfoPart2(Method method, InfoPart1 part1) {
            this.method = method;
            this.fallbackImpl = part1.fallbackImpl;
            this.serviceId = part1.serviceId;
            this.timeoutInSec = part1.timeoutInSec;
        }

        InvokeInfo toNewInfo(Object[] args) {
            return new InvokeInfo(method, args, fallbackImpl, serviceId, timeoutInSec);
        }
    }

    @RequiredArgsConstructor
    private static class InvokeInfo {
        final Method method;
        final Object[] args;
        final Object fallbackImpl;
        final String serviceId;
        final int timeoutInSec;
        String group;

        {
            group = RpcProxySelector.getAndRemoveGroup();
        }

    }

    @Slf4j
    private static class CacheLogWriter implements ILogWriter {
        @Override
        public void onWriteInfo(String s) {
            log.info(s);
        }

        @Override
        public void onWriteWarn(String s) {
            log.warn(s);
        }
    }

    @RequiredArgsConstructor
    private static class PickerWrapper implements DataPicker<RpcServerInfo> {

        private final DataPicker<RpcServerInfo> target;
        private final RpcServerInfo info;

        @Override
        public void set(List<RpcServerInfo> list) {
            throw new RpcPluginException("不支持set方法");
        }

        @Override
        public RpcServerInfo getNext() {
            return info;
        }

        @Override
        public List<RpcServerInfo> getAllUsable() {
            return Collections.singletonList(info);
        }

        @Override
        public void onUnusable(RpcServerInfo data) {
            DataPicker.super.onUnusable(data);
            target.onUnusable(data);
        }
    }

}
