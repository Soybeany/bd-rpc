package com.soybeany.sync.client;

import com.google.gson.reflect.TypeToken;
import com.soybeany.sync.client.api.IClientPlugin;
import com.soybeany.sync.client.api.ISyncClientConfig;
import com.soybeany.sync.client.api.ISyncExceptionWatcher;
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
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.soybeany.sync.core.util.NetUtils.GSON;

/**
 * @author Soybeany
 * @date 2021/10/27
 */
@Slf4j
public class SyncClientService {

    private static final Type TYPE = new TypeToken<Map<String, String>>() {
    }.getType();

    @SuppressWarnings("AlibabaThreadPoolCreation")
    private final ScheduledExecutorService service = Executors.newScheduledThreadPool(1);

    private final ISyncClientConfig config;
    @Getter
    private final DataPicker<String> urlPicker;
    private final List<IClientPlugin<Object, Object>> allPlugins;
    private final ISyncExceptionWatcher watcher;

    private Boolean started;

    public SyncClientService(ISyncClientConfig config, IClientPlugin<Object, Object>[] plugins, ISyncExceptionWatcher watcher) {
        this.config = config;
        urlPicker = config.onSetupSyncServerPicker();
        allPlugins = Arrays.asList(plugins);
        this.watcher = watcher;
        IBasePlugin.checkPlugins(allPlugins);
        Collections.sort(allPlugins);
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
        int interval = config.onSetupSyncIntervalInSec();
        SyncClientInfo info = new SyncClientInfo(interval, config.onSetupSyncTimeoutInSec());
        // 插件预处理
        allPlugins.forEach(plugin -> plugin.onPreTreat(allPlugins));
        // 启动回调
        allPlugins.forEach(plugin -> plugin.onStartup(info));
        // 执行定时任务
        service.scheduleWithFixedDelay(this::sendSync, 0, interval, TimeUnit.SECONDS);
    }

    private void onStop() {
        allPlugins.forEach(IClientPlugin::onShutdown);
        service.shutdown();
    }

    private void sendSync() {
        String uid = BdFileUtils.getUuid();
        RequestUtils.Config rConfig = new RequestUtils.Config();
        rConfig.setTimeoutInSec(config.onSetupSyncTimeoutInSec());
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
