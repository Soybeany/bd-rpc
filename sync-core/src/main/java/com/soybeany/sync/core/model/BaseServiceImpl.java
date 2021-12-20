package com.soybeany.sync.core.model;

import java.util.List;

/**
 * 基础的服务实现，需要注册为spring bean来使用
 *
 * @author Soybeany
 * @date 2021/12/16
 */
public abstract class BaseServiceImpl<Plugin> {

    // ***********************子类工具方法****************************

    @SuppressWarnings("SameParameterValue")
    protected String getUrl(boolean secure, String ip, int port, String context, String path, String suffix) {
        String protocol = secure ? "https" : "http";
        return protocol + "://" + ip + ":" + port + context + path + suffix;
    }

    // ***********************公共方法****************************

    public void start() {
        onStart();
    }

    public void stop() {
        onStop();
    }

    // ***********************子类重写****************************

    protected void onStart() {
    }

    protected void onStop() {
    }

    // ***********************子类实现****************************

    protected abstract void onSetupPlugins(List<Plugin> plugins);

}
