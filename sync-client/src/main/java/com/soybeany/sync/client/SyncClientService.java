package com.soybeany.sync.client;

import com.google.gson.reflect.TypeToken;
import com.soybeany.sync.core.api.IBasePlugin;
import com.soybeany.sync.core.api.IClientPlugin;
import com.soybeany.sync.core.api.ISyncClientConfig;
import com.soybeany.sync.core.exception.SyncException;
import com.soybeany.sync.core.exception.SyncRequestException;
import com.soybeany.sync.core.model.SyncClientInfo;
import com.soybeany.sync.core.model.SyncDTO;
import com.soybeany.sync.core.util.RequestUtils;
import com.soybeany.util.file.BdFileUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.soybeany.sync.core.util.RequestUtils.GSON;

/**
 * @author Soybeany
 * @date 2021/10/27
 */
@Slf4j
public class SyncClientService {

    @SuppressWarnings("AlibabaThreadPoolCreation")
    private final ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
    private final Type type = new TypeToken<Map<String, String>>() {
    }.getType();

    private final ISyncClientConfig config;
    private final List<IClientPlugin<Object, Object>> allPlugins;

    private Boolean started;

    public SyncClientService(ISyncClientConfig config, IClientPlugin<Object, Object>[] plugins) {
        this.config = config;
        allPlugins = Arrays.asList(plugins);
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
        try {
            onSendSync(uid);
        } catch (Exception e) {
            allPlugins.forEach(plugin -> handleException(plugin, uid, e));
        }
    }

    private void onSendSync(String uid) throws Exception {
        RequestUtils.Config rConfig = new RequestUtils.Config();
        rConfig.setTimeoutInSec(config.onSetupSyncTimeoutInSec());
        Set<String> syncPlugins = new HashSet<>();
        // 同步前回调
        for (IClientPlugin<Object, Object> plugin : allPlugins) {
            Object tmpOutput = plugin.onGetOutputClass().getConstructor().newInstance();
            try {
                boolean needSync = plugin.onBeforeSync(uid, tmpOutput);
                if (needSync) {
                    String tag = plugin.onSetupSyncTagToHandle();
                    syncPlugins.add(tag);
                    rConfig.getParams().put(tag, GSON.toJson(tmpOutput));
                }
            } catch (Exception e) {
                handleException(plugin, uid, e);
            }
        }
        // 执行同步
        SyncDTO dto = RequestUtils.request(config.onGetSyncServerPicker(), url -> url, rConfig, SyncDTO.class, "暂无可用的注册中心");
        if (!dto.getIsNorm()) {
            throw new SyncRequestException(dto.getParsedErrMsg());
        }
        // 同步后回调
        Map<String, String> result = dto.getData(type);
        for (IClientPlugin<Object, Object> plugin : allPlugins) {
            String tag = plugin.onSetupSyncTagToHandle();
            if (!syncPlugins.contains(tag)) {
                continue;
            }
            String tagInputJson = result.get(tag);
            if (null == tagInputJson) {
                handleException(plugin, uid, new SyncException("缺失“" + tag + "”的同步数据"));
                continue;
            }
            try {
                plugin.onAfterSync(uid, GSON.fromJson(tagInputJson, plugin.onGetInputClass()));
            } catch (Exception e) {
                handleException(plugin, uid, e);
            }
        }
    }

    private void handleException(IClientPlugin<Object, Object> plugin, String uid, Exception e) {
        try {
            plugin.onSyncException(uid, e);
        } catch (Exception e2) {
            log.error(e2.getMessage());
        }
    }

}
