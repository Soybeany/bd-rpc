package com.soybeany.sync.core.model;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 基础的服务实现，需要注册为spring bean，并手工调用{@link #start()}与{@link #stop()}
 *
 * @author Soybeany
 * @date 2021/12/16
 */
public abstract class BaseSyncerImpl<Plugin> {

    // ***********************子类工具方法****************************

    @SuppressWarnings("SameParameterValue")
    protected String getUrl(boolean secure, String ip, int port, String context, String path, String suffix) {
        String protocol = secure ? "https" : "http";
        return protocol + "://" + ip + ":" + port + context + path + suffix;
    }

    protected String[] getUrls(String[] hosts, String path) {
        String[] urls = new String[hosts.length];
        for (int i = 0; i < hosts.length; i++) {
            urls[i] = hosts[i] + path;
        }
        return urls;
    }

    protected List<String> getObjNames(List<?> objs) {
        return objs.stream()
                .map(obj -> obj.getClass().getSimpleName())
                .collect(Collectors.toList());
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

    /**
     * 配置待使用的插件
     */
    protected abstract void onSetupPlugins(List<Plugin> plugins);

}
