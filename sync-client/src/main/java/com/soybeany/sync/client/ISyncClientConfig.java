package com.soybeany.sync.client;

/**
 * @author Soybeany
 * @date 2021/11/2
 */
public interface ISyncClientConfig {

    /**
     * 获取同步服务器的url
     *
     * @return 值
     */
    String onGetSyncServerUrl();

    /**
     * 设置同步间隔
     *
     * @return 秒值
     */
    int onSetupSyncIntervalInSec();

}
