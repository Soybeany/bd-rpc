package com.soybeany.sync.core.util;

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

}
