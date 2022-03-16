package com.soybeany.sync.client.impl;

import com.soybeany.sync.client.SyncClientService;
import com.soybeany.sync.client.api.IClientPlugin;
import com.soybeany.sync.client.api.IExClientPluginProvider;
import com.soybeany.sync.client.api.ISyncExceptionAware;
import com.soybeany.sync.client.api.ISyncer;
import com.soybeany.sync.client.model.SyncClientInfo;
import com.soybeany.sync.client.model.SyncState;
import com.soybeany.sync.client.picker.DataPicker;
import com.soybeany.sync.core.model.BaseSyncerImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

/**
 * @author Soybeany
 * @date 2021/12/16
 */
@Slf4j
public abstract class BaseClientSyncerImpl extends BaseSyncerImpl<IClientPlugin<?, ?>> implements ISyncExceptionAware, ISyncer, ServletContextListener {

    private static final Set<String> EXIST_SYNCER_IDS = new HashSet<>();

    @Autowired
    protected ApplicationContext appContext;
    @Autowired(required = false)
    private List<IExClientPluginProvider> pluginProviders;

    protected SyncClientService service;

    @SuppressWarnings("unchecked")
    protected static List<IClientPlugin<Object, Object>> toPluginArr(List<IClientPlugin<?, ?>> list) {
        return Arrays.asList(list.toArray(new IClientPlugin[0]));
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        service.getAllPlugins().forEach(IClientPlugin::onApplicationStarted);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // syncerId检查
        String syncerId = onSetupSyncerId();
        checkSyncerId(syncerId);
        // 插件配置及回调
        List<IClientPlugin<?, ?>> plugins = new ArrayList<>();
        onSetupPlugins(syncerId, plugins);
        Optional.ofNullable(pluginProviders)
                .ifPresent(providers -> providers.forEach(provider -> provider.onSetupPlugins(syncerId, plugins)));
        postSetupPlugins(Collections.unmodifiableList(plugins));
        // 启动服务
        SyncClientInfo info = new SyncClientInfo(appContext, onSetupSystem(), onSetupVersion(),
                onSetupSyncIntervalSec(), onSetupSyncTimeoutSec());
        service = new SyncClientService(info, onSetupSyncServerPicker(), toPluginArr(plugins), this);
        service.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        service.stop();
    }

    @Override
    public void onSyncException(List<IClientPlugin<Object, Object>> plugins, String uid, SyncState state, Exception e) {
        log.warn("同步异常" + getObjNames(plugins) + "(" + state + "): " + e.getMessage());
    }

    @Override
    public void sync(boolean async) {
        service.sync(async);
    }

    // ***********************内部方法****************************

    private void checkSyncerId(String syncerId) {
        boolean added = EXIST_SYNCER_IDS.add(syncerId);
        if (!added) {
            throw new RuntimeException("同时配置多个syncer时，每个syncer需使用onSetupSyncerId配置不同的id");
        }
    }

    // ***********************子类重写****************************

    /**
     * 配置服务所在的系统，系统间的服务与数据都是隔离的
     */
    protected String onSetupSystem() {
        return null;
    }

    /**
     * 当前服务的版本，当有 “api/实现” 不可兼容变更时，可改变版本号值进行隔离
     */
    protected String onSetupVersion() {
        return "0";
    }

    /**
     * 配置同步的等待超时
     */
    protected int onSetupSyncTimeoutSec() {
        return 10;
    }

    /**
     * 设置同步服务器的url
     *
     * @return 值
     */
    protected abstract DataPicker<String> onSetupSyncServerPicker();

    /**
     * 设置同步间隔
     *
     * @return 秒值
     */
    protected abstract int onSetupSyncIntervalSec();

}
