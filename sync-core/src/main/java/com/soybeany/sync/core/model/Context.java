package com.soybeany.sync.core.model;

import com.soybeany.sync.core.util.NetUtils;
import lombok.Getter;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Soybeany
 * @date 2021/10/27
 */
@Getter
public class Context {

    // **请求用**

    private String ipAddress;
    private final Map<String, String> headers = new HashMap<>();

    // **内部用**

    private final Map<String, Object> data = new HashMap<>();

    // ***********************方法区****************************

    public static Context getEmpty() {
        Context ctx = new Context();
        ctx.ipAddress = NetUtils.getLocalIpAddress();
        return ctx;
    }

    public static Context fromRequest(HttpServletRequest request) {
        Context ctx = new Context();
        ctx.ipAddress = NetUtils.getIpAddressFromRequest(request);
        Enumeration<String> keys = request.getHeaderNames();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            ctx.headers.put(key, request.getHeader(key));
        }
        return ctx;
    }

}
