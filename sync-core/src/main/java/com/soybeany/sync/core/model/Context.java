package com.soybeany.sync.core.model;

import lombok.Getter;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Soybeany
 * @date 2021/10/27
 */
@Getter
public class Context {

    private String ipAddress;
    private Map<String, String> requestHeaders;
    private final Map<String, Object> data = new HashMap<>();

    // ***********************方法区****************************

    public static Context getNew(HttpServletRequest request) {
        Context ctx = new Context();
        ctx.ipAddress = getIpAddress(request);
        ctx.requestHeaders = getHeaders(request);
        return ctx;
    }

    // ***********************内部方法****************************

    private static Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> result = new HashMap<>();
        Enumeration<String> keys = request.getHeaderNames();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            result.put(key, request.getHeader(key));
        }
        return Collections.unmodifiableMap(result);
    }

    private static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (hasNoIp(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (hasNoIp(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (hasNoIp(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    private static boolean hasNoIp(String ip) {
        return ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip);
    }

}
