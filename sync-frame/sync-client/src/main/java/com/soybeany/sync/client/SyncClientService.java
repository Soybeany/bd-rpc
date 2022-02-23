package com.soybeany.sync.client;

import com.google.gson.reflect.TypeToken;
import com.soybeany.sync.client.api.IClientPlugin;
import com.soybeany.sync.client.api.ISyncExceptionWatcher;
import com.soybeany.sync.client.api.ISyncer;
import com.soybeany.sync.client.model.SyncClientInfo;
import com.soybeany.sync.client.model.SyncState;
import com.soybeany.sync.client.picker.DataPicker;
import com.soybeany.sync.client.util.RequestUtils;
import com.soybeany.sync.core.api.IBasePlugin;
import com.soybeany.sync.core.exception.SyncException;
import com.soybeany.sync.core.exception.SyncRequestException;
import com.soybeany.sync.core.model.SyncDTO;
import com.soybeany.util.file.BdFileUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.soybeany.sync.core.util.NetUtils.GSON;

/**
 * @author Soybeany
 * @date 2021/10/27
 */
@Slf4j
@RequiredArgsConstructor
public class SyncClientService implements ISyncer {

    private static final Type TYPE = new TypeToken<Map<String, String>>() {
    }.getType();

    @SuppressWarnings("AlibabaThreadPoolCreation")
    private final ScheduledExecutorService service = Executors.newScheduledThreadPool(1);

    private final SyncClientInfo info;
    @Getter
    private final DataPicker<String> urlPicker;
    private final List<IClientPlugin<Object, Object>> allPlugins;
    private final ISyncExceptionWatcher watcher;

    private Boolean started;

    @Override
    public void sync(boolean async) {
        if (async) {
            service.submit(this::onSync);
        } else {
            onSync();
        }
    }

    public void start() {
        if (null != started) {
            throw new RuntimeException(getClass().getName() + "只能启动一次");
        }
        this.started = true;
        onStart();
    }

    public void stop() {
        if (!started) {
            throw new RuntimeException(getClass().getName() + "未启动");
        }
        this.started = false;
        onStop();
    }

    // ***********************内部方法****************************

    private void onStart() {
        // 插件预处理
        IBasePlugin.checkPlugins(allPlugins);
        Collections.sort(allPlugins);
        // 插件预处理
        allPlugins.forEach(plugin -> plugin.onPreTreat(allPlugins));
        // 启动回调
        allPlugins.forEach(plugin -> plugin.onStartup(info));
        // 执行定时任务
        service.scheduleWithFixedDelay(this::safeSync, 0, info.getSyncIntervalSec(), TimeUnit.SECONDS);
    }

    private void onStop() {
        allPlugins.forEach(IClientPlugin::onShutdown);
        service.shutdown();
    }

    private void safeSync() {
        try {
            onSync();
        } catch (Throwable e) {
            log.error("同步出现了未预料的异常:" + e.getMessage());
        }
    }

    private synchronized void onSync() {
        String uid = BdFileUtils.getUuid();
        RequestUtils.Config rConfig = new RequestUtils.Config();
        rConfig.setTimeoutSec(info.getSyncTimeoutSec());
        List<IClientPlugin<Object, Object>> syncPlugins = new ArrayList<>();
        // 同步前回调
        for (IClientPlugin<Object, Object> plugin : allPlugins) {
            try {
                Object tmpOutput = plugin.onGetOutputClass().getConstructor().newInstance();
                boolean needSync = plugin.onBeforeSync(uid, tmpOutput);
                if (needSync) {
                    rConfig.getParams().put(plugin.onSetupSyncTagToHandle(), GSON.toJson(tmpOutput));
                    syncPlugins.add(plugin);
                }
            } catch (Exception e) {
                handleException(Collections.singletonList(plugin), uid, SyncState.BEFORE, e);
            }
        }
        // 若没有需要同步的插件，则直接返回
        if (syncPlugins.isEmpty()) {
            return;
        }
        // 执行同步
        SyncDTO dto;
        Map<String, String> data;
        try {
            RequestUtils.Result<String, SyncDTO> result = RequestUtils.request(urlPicker, url -> url, rConfig, SyncDTO.class, "暂无可用的注册中心");
            dto = result.getData();
            if (null == dto) {
                throw new SyncRequestException("注册中心的sync接口(" + result.getUrl() + ")未设置返回值");
            }
            if (!dto.getIsNorm()) {
                throw new SyncRequestException(dto.parseErrMsg() + "(" + result.getUrl() + ")");
            }
            data = dto.toData(TYPE);
        } catch (Exception e) {
            handleException(syncPlugins, uid, SyncState.SYNC, e);
            return;
        }
        // 同步后回调
        for (IClientPlugin<Object, Object> plugin : syncPlugins) {
            String tag = plugin.onSetupSyncTagToHandle();
            String tagInputJson = data.get(tag);
            if (null == tagInputJson) {
                handleException(Collections.singletonList(plugin), uid, SyncState.RECEIVE, new SyncException("缺失“" + tag + "”的同步数据"));
                continue;
            }
            try {
                plugin.onAfterSync(uid, GSON.fromJson(tagInputJson, plugin.onGetInputClass()));
            } catch (Exception e) {
                handleException(Collections.singletonList(plugin), uid, SyncState.AFTER, e);
            }
        }
    }

    private void handleException(List<IClientPlugin<Object, Object>> plugins, String uid, SyncState state, Exception e) {
        // 插件回调
        for (IClientPlugin<Object, Object> plugin : plugins) {
            try {
                plugin.onSyncException(uid, state, e);
            } catch (Exception e2) {
                log.error(e2.getMessage());
            }
        }
        // 执行异常监控回调
        try {
            watcher.onSyncException(plugins, uid, state, e);
        } catch (Exception e2) {
            log.error(e2.getMessage());
        }
    }

}
