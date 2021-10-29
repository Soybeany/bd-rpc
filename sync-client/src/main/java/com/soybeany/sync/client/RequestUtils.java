package com.soybeany.sync.client;

import com.google.gson.Gson;
import okhttp3.*;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author Soybeany
 * @date 2021/10/29
 */
public class RequestUtils {

    private static final OkHttpClient CLIENT = new OkHttpClient();
    private static final Gson GSON = new Gson();

    @Nullable
    public static <T> T request(String url, Map<String, String> headers, Map<String, String> params, Type type) {
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
            return GSON.fromJson(response.body().string(), type);
        } catch (IOException e) {
            return null;
        }
    }

}
