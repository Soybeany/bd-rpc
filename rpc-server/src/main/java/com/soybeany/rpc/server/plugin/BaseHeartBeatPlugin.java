package com.soybeany.rpc.server.plugin;

import com.soybeany.rpc.core.IPreTreatServicePlugin;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Soybeany
 * @date 2021/10/26
 */
public abstract class BaseHeartBeatPlugin implements IPreTreatServicePlugin {

    @Override
    public void onHandle(HttpServletRequest request, Map<String, String[]> param, Map<String, String> result) {
        onStorageHeartBeatInfo(getIpAddr(request));
    }

    protected abstract void onStorageHeartBeatInfo(String ip);

    // ***********************内部方法****************************

    private String getIpAddr(HttpServletRequest request) {
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

    private boolean hasNoIp(String ip) {
        return ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip);
    }

}
