package com.soybeany.sync.core.model;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 基础的服务实现，需要注册为spring bean
 *
 * @author Soybeany
 * @date 2021/12/16
 */
public abstract class BaseSyncerImpl<Plugin> implements InitializingBean, DisposableBean {

    @Override
    public void afterPropertiesSet() {
        onStart();
    }

    @Override
    public void destroy() {
        onStop();
    }

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

    /**
     * 设置完插件后的回调
     */
    protected void postSetupPlugins(List<Plugin> plugins) {
    }

}
