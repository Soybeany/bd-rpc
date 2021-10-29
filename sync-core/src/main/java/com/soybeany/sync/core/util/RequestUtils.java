package com.soybeany.sync.core.util;

import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author Soybeany
 * @date 2021/10/29
 */
public class RequestUtils {

    public static final Gson GSON = new Gson();
    private static final OkHttpClient CLIENT = new OkHttpClient();

    public static <T> T request(String url, Map<String, String> headers, Map<String, String> params, Class<T> resultClass) {
        String bodyString = getBodyString(url, headers, params);
        if (null == bodyString) {
            return null;
        }
        return GSON.fromJson(bodyString, resultClass);
    }

    /**
     * 单url请求
     */
    public static <T> T request(String url, Map<String, String> headers, Map<String, String> params, Type resultType) {
        String bodyString = getBodyString(url, headers, params);
        if (null == bodyString) {
            return null;
        }
        return GSON.fromJson(bodyString, resultType);
    }

    // ***********************内部方法****************************

    private static String getBodyString(String url, Map<String, String> headers, Map<String, String> params) {
        Request.Builder requestBuilder = new Request.Builder().url(url);
        // 添加header
        headers.forEach(requestBuilder::header);
        // 添加入参
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        params.forEach(bodyBuilder::add);
        requestBuilder.post(bodyBuilder.build());
        // 请求
        Call call = CLIENT.newCall(requestBuilder.build());
        try (Response response = call.execute()) {
            if (!response.isSuccessful() || null == response.body()) {
                return null;
            }
            return response.body().string();
        } catch (IOException e) {
            return null;
        }
    }

}
