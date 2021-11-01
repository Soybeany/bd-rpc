package com.soybeany.sync.client;

import com.google.gson.reflect.TypeToken;
import com.soybeany.sync.core.api.IClientPlugin;
import com.soybeany.sync.core.model.Context;
import com.soybeany.sync.core.model.SyncSender;
import com.soybeany.sync.core.util.RequestUtils;
import com.soybeany.sync.core.util.TagUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Soybeany
 * @date 2021/10/27
 */
@Service
public class MainService implements SyncSender {

    @SuppressWarnings("AlibabaThreadPoolCreation")
    private final ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
    private final Type type = new TypeToken<Map<String, String>>() {
    }.getType();

    @Autowired
    private List<IClientPlugin> allPlugins;

    @Override
    public void send(Map<String, String> data) {
        service.execute(() -> sendSync(data));
    }

    // ***********************内部方法****************************

    @PostConstruct
    private void onStart() {
        // 排序
        Collections.sort(allPlugins);
        // 启动回调
        allPlugins.forEach(plugin -> plugin.onStartup(this));
        // 执行定时任务
        service.scheduleWithFixedDelay(() -> sendSync(null), 0, 3, TimeUnit.SECONDS);
    }

    @PreDestroy
    private void onDestroy() {
        service.shutdown();
    }

    private void sendSync(Map<String, String> data) {
        Context ctx = Context.getEmpty();
        if (null != data) {
            ctx.getData().putAll(data);
        }
        Map<String, String> params = new HashMap<>();
        for (IClientPlugin plugin : allPlugins) {
            Map<String, String> tmpParam = new HashMap<>();
            plugin.onSendSync(ctx, tmpParam);
            String tag = plugin.onSetupSyncTagToHandle();
            tmpParam.forEach((k, v) -> params.put(TagUtils.addTag(tag, k), v));
        }
        Map<String, String> result = RequestUtils.request("http://localhost:8080/api/sync", ctx.getHeaders(), params, type);
        if (null == result) {
            return;
        }
        Map<String, Map<String, String>> paramMap = TagUtils.split(result);
        for (IClientPlugin plugin : allPlugins) {
            String tag = plugin.onSetupSyncTagToHandle();
            Map<String, String> tagResult = paramMap.get(tag);
            if (null == tagResult) {
                continue;
            }
            plugin.onHandleSync(ctx, tagResult);
        }
    }
}
