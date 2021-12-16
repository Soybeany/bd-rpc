package com.soybeany.sync.client;

import com.google.gson.reflect.TypeToken;
import com.soybeany.sync.core.api.IClientPlugin;
import com.soybeany.sync.core.api.ISyncClientConfig;
import com.soybeany.sync.core.exception.SyncRequestException;
import com.soybeany.sync.core.model.SyncDTO;
import com.soybeany.sync.core.util.RequestUtils;
import com.soybeany.sync.core.util.TagUtils;
import lombok.extern.java.Log;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Soybeany
 * @date 2021/10/27
 */
@Log
public class SyncClientService {

    @SuppressWarnings("AlibabaThreadPoolCreation")
    private final ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
    private final Type type = new TypeToken<Map<String, String>>() {
    }.getType();

    private final ISyncClientConfig config;
    private final List<IClientPlugin> allPlugins;

    private Boolean started;

    public SyncClientService(ISyncClientConfig config, IClientPlugin... plugins) {
        this.config = config;
        this.allPlugins = Arrays.asList(plugins);
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
        // 启动回调
        service.submit(() -> allPlugins.forEach(IClientPlugin::onStartup));
        // 执行定时任务
        service.scheduleWithFixedDelay(this::sendSync, 0, config.onSetupSyncIntervalInSec(), TimeUnit.SECONDS);
    }

    private void onStop() {
        service.shutdown();
    }

    private void sendSync() {
        Map<String, String> params = new HashMap<>();
        for (IClientPlugin plugin : allPlugins) {
            Map<String, String> tmpParam = new HashMap<>();
            plugin.onSendSync(tmpParam);
            String tag = plugin.onSetupSyncTagToHandle();
            tmpParam.forEach((k, v) -> params.put(TagUtils.addTag(tag, k), v));
        }
        SyncDTO dto;
        try {
            dto = RequestUtils.request(config.onGetSyncServerPicker(), url -> url, null, params, SyncDTO.class, "暂无可用的注册中心");
        } catch (SyncRequestException e) {
            log.warning(e.getMessage());
            return;
        }
        if (!dto.getIsNorm()) {
            log.warning(dto.getParsedErrMsg());
            return;
        }
        Map<String, String> result = dto.getData(type);
        Map<String, Map<String, String>> paramMap = TagUtils.split(result);
        for (IClientPlugin plugin : allPlugins) {
            String tag = plugin.onSetupSyncTagToHandle();
            Map<String, String> tagResult = paramMap.get(tag);
            if (null == tagResult) {
                continue;
            }
            plugin.onHandleSync(tagResult);
        }
    }

}
