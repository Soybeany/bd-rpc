package com.soybeany.api.gateway;

import com.soybeany.util.file.BdFileUtils;
import okhttp3.*;
import okhttp3.internal.http.HttpMethod;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Soybeany
 * @date 2022/1/18
 */
public class ReverseProxyUtils {

    private static final OkHttpClient CLIENT = new OkHttpClient();
    private static final Pattern PATH_PATTERN = Pattern.compile("(/.+)(/.+)");

    public static void start(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uri = request.getRequestURI();
        String path = uri.substring(request.getContextPath().length());
        String method = request.getMethod();
        RequestBody requestBody = HttpMethod.permitsRequestBody(method) ? createBody(request.getContentType(), request.getInputStream()) : null;
        Request nRequest = new Request.Builder()
                .url("http://localhost:8082/test")
                .method(method, requestBody)
                .build();
        Response nResponse = CLIENT.newCall(nRequest).execute();
        ResponseBody body = Optional.ofNullable(nResponse.body()).orElseThrow(() -> new IOException("body为空"));
        for (String name : nResponse.headers().names()) {
            System.out.println(name + ": " + nResponse.header(name));
            response.setHeader(name, nResponse.header(name));
        }
        BdFileUtils.readWriteStream(body.byteStream(), response.getOutputStream());
    }

    private static String[] split(String path) throws IOException {
        Matcher matcher = PATH_PATTERN.matcher(path);
        if (!matcher.find()) {
            throw new IOException("请求路径格式异常");
        }
        return new String[]{matcher.group(1), matcher.group(2)};
    }

    private static RequestBody createBody(String contentType, InputStream stream) {
        return new okhttp3.RequestBody() {
            @Override
            public MediaType contentType() {
                return MediaType.get(contentType);
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                try (Source source = Okio.source(stream)) {
                    sink.writeAll(source);
                }
            }
        };
    }

}
