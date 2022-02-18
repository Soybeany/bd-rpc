package com.soybeany.sync.core.util;

import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Soybeany
 * @date 2021/11/1
 */
public class NetUtils {

    public static final Gson GSON = new Gson();
    public static final String LOCAL_IPV4 = "127.0.0.1";
    public static final String LOCAL_IPV6 = "0:0:0:0:0:0:0:1";

    public static String getLocalIpAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException("无法获取本地ip地址");
        }
    }

    public static String getRemoteIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (LOCAL_IPV4.equals(ipAddress) || LOCAL_IPV6.equals(ipAddress)) {
                ipAddress = getLocalIpAddress();
            }
        }
        //多层代理情况下，取最右边的ip，如：1.2.3.4, 1.5.6.7
        if (ipAddress != null && !"".equals(ipAddress)) {
            String[] parts = ipAddress.split("\\s*,\\s*");
            ipAddress = parts[parts.length - 1];
        }
        return null != ipAddress ? ipAddress.trim() : null;
    }

}
