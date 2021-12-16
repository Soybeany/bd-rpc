package com.soybeany.sync.core.api;

import lombok.AllArgsConstructor;

/**
 * @author Soybeany
 * @date 2021/11/2
 */
public interface ISyncClientConfig {

    // TODO: 2021/12/16 增加Picker配置

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

    @AllArgsConstructor
    class StdImpl implements ISyncClientConfig {

        private String syncServerUrl;
        private int syncIntervalInSec;

        @Override
        public String onGetSyncServerUrl() {
            return syncServerUrl;
        }

        @Override
        public int onSetupSyncIntervalInSec() {
            return syncIntervalInSec;
        }
    }

}
