package com.soybeany.sync.core.util;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Soybeany
 * @date 2021/11/1
 */
public class NetUtils {

    public static String getLocalIpAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException("无法获取本地ip地址");
        }
    }

    public static String getIpAddressFromRequest(HttpServletRequest request) {
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
