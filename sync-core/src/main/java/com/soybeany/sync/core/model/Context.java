package com.soybeany.sync.core.model;

import lombok.Getter;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
    private final Map<String, String> requestHeaders = new HashMap<>();
    private final Map<String, Object> data = new HashMap<>();

    // ***********************方法区****************************

    public static Context getEmpty() {
        Context ctx = new Context();
        try {
            ctx.ipAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException("无法获取本地ip地址");
        }
        return ctx;
    }

    public static Context fromRequest(HttpServletRequest request) {
        Context ctx = new Context();
        ctx.ipAddress = getIpAddress(request);
        setupHeaders(request, ctx.requestHeaders);
        return ctx;
    }

    // ***********************内部方法****************************

    private static void setupHeaders(HttpServletRequest request, Map<String, String> headers) {
        Enumeration<String> keys = request.getHeaderNames();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            headers.put(key, request.getHeader(key));
        }
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
