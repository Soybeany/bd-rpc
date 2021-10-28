package com.soybeany.sync.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.soybeany.sync.core.api.IClientPlugin;
import com.soybeany.sync.core.model.Context;
import com.soybeany.sync.core.util.TagUtils;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
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
public class MainService {

    @SuppressWarnings("AlibabaThreadPoolCreation")
    private final ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();
    private final Type type = new TypeToken<Map<String, String>>() {
    }.getType();

    @Autowired
    private List<IClientPlugin> allPlugins;

    @PostConstruct
    private void onStart() {
        // 排序
        Collections.sort(allPlugins);
        // 执行定时任务
        service.scheduleWithFixedDelay(() -> {
            Context ctx = Context.getEmpty();
            Map<String, String> param = new HashMap<>();
            for (IClientPlugin plugin : allPlugins) {
                Map<String, String> tmpParam = new HashMap<>();
                plugin.onSendSync(ctx, tmpParam);
                String tag = plugin.onSetupSyncTagToHandle();
                tmpParam.forEach((k, v) -> param.put(TagUtils.addTag(tag, k), v));
            }
            Map<String, String> result = sendRequest(ctx, param);
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
        }, 0, 3, TimeUnit.SECONDS);
    }

    @Nullable
    private Map<String, String> sendRequest(Context ctx, Map<String, String> param) {
        Request.Builder requestBuilder = new Request.Builder()
                .url("http://localhost:8080/api/sync");
        // 添加header
        ctx.getRequestHeaders().forEach(requestBuilder::header);
        // 添加入参
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        param.forEach(bodyBuilder::add);
        requestBuilder.post(bodyBuilder.build());
        // 请求
        Call call = client.newCall(requestBuilder.build());
        try (Response response = call.execute()) {
            if (!response.isSuccessful() || null == response.body()) {
                return null;
            }
            return gson.fromJson(response.body().string(), type);
        } catch (IOException e) {
            return null;
        }
    }

}
