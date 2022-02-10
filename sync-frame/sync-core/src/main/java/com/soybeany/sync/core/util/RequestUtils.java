package com.soybeany.sync.core.util;

import com.google.gson.Gson;
import com.soybeany.sync.core.exception.SyncRequestException;
import com.soybeany.sync.core.picker.DataPicker;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author Soybeany
 * @date 2021/10/29
 */
@Slf4j
public class RequestUtils {

    public static final Gson GSON = new Gson();
    private static final Map<Integer, OkHttpClient> CLIENT_MAP = new WeakHashMap<>();

    public static <D, T> Result<D, T> request(DataPicker<D> urlPicker, Function<D, String> urlMapper, Config config, Type resultType, String errMsg) throws SyncRequestException {
        for (D url : urlPicker.getAllUsable()) {
            String urlStr = urlMapper.apply(url);
            try {
                String bodyString = getBodyString(urlStr, config);
                return new Result<>(url, GSON.fromJson(bodyString, resultType));
            } catch (Exception e) {
                log.warn("请求“" + urlStr + "”异常(" + e.getMessage() + ")");
                urlPicker.onUnusable(url);
            }
        }
        log.warn(errMsg);
        throw new SyncRequestException(errMsg);
    }

    // ***********************内部方法****************************

    private static String getBodyString(String url, Config config) throws SyncRequestException {
        Request.Builder requestBuilder;
        try {
            requestBuilder = new Request.Builder().url(url);
        } catch (IllegalArgumentException e) {
            throw new SyncRequestException("请求参数异常，无法访问");
        }
        // 添加header
        config.headers.forEach(requestBuilder::header);
        // 添加入参
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        config.params.forEach((k, v) -> Optional.ofNullable(v).ifPresent(v1 -> bodyBuilder.add(k, v1)));
        requestBuilder.post(bodyBuilder.build());
        // 请求
        OkHttpClient client;
        synchronized (CLIENT_MAP) {
            client = CLIENT_MAP.computeIfAbsent(config.timeoutInSec, timeout -> new OkHttpClient.Builder()
                    .writeTimeout(timeout, TimeUnit.SECONDS)
                    .readTimeout(timeout, TimeUnit.SECONDS)
                    .build()
            );
        }
        Call call = client.newCall(requestBuilder.build());
        try (Response response = call.execute()) {
            if (!response.isSuccessful()) {
                throw new SyncRequestException("请求失败:" + response.code());
            }
            if (null == response.body()) {
                throw new SyncRequestException("body为null");
            }
            return response.body().string();
        } catch (IOException e) {
            throw new SyncRequestException(e.getMessage());
        }
    }

    @Data
    public static class Config {
        private final Map<String, String> headers = new HashMap<>();
        private final Map<String, String> params = new HashMap<>();
        private int timeoutInSec = 10;
    }

    @Data
    public static class Result<D, T> {
        private final D url;
        private final T data;
    }

}
