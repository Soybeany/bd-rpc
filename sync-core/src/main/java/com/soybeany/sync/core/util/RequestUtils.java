package com.soybeany.sync.core.util;

import com.google.gson.Gson;
import com.soybeany.sync.core.exception.SyncRequestException;
import com.soybeany.sync.core.picker.DataPicker;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author Soybeany
 * @date 2021/10/29
 */
public class RequestUtils {

    public static final Gson GSON = new Gson();
    private static final OkHttpClient CLIENT = new OkHttpClient();

    public static <D, T> T request(DataPicker<D> picker, Function<D, String> urlMapper, Map<String, String> headers, Map<String, String> params, Type resultType, String errMsg) throws SyncRequestException {
        for (D data : picker) {
            //
            if (null == data) {
                continue;
            }
            try {
                String bodyString = getBodyString(urlMapper.apply(data), headers, params);
                return GSON.fromJson(bodyString, resultType);
            } catch (Exception e) {
                picker.onUnusable(data);
            }
        }
        throw new SyncRequestException(errMsg);
    }

    // ***********************内部方法****************************

    private static String getBodyString(String url, Map<String, String> headers, Map<String, String> params) throws SyncRequestException {
        Request.Builder requestBuilder;
        try {
            requestBuilder = new Request.Builder().url(url);
        } catch (IllegalArgumentException e) {
            throw new SyncRequestException("服务提供者参数异常，无法访问");
        }
        // 添加header
        if (null != headers) {
            headers.forEach(requestBuilder::header);
        }
        // 添加入参
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        if (null != params) {
            params.forEach((k, v) -> Optional.ofNullable(v).ifPresent(v1 -> bodyBuilder.add(k, v1)));
        }
        requestBuilder.post(bodyBuilder.build());
        // 请求
        Call call = CLIENT.newCall(requestBuilder.build());
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

}
