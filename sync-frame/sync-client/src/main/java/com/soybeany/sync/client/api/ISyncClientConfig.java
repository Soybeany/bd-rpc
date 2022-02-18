package com.soybeany.sync.client.api;

import com.soybeany.sync.client.picker.DataPicker;
import com.soybeany.sync.client.picker.DataPickerSimpleImpl;
import lombok.AllArgsConstructor;

/**
 * @author Soybeany
 * @date 2021/11/2
 */
public interface ISyncClientConfig {

    /**
     * 设置同步服务器的url
     *
     * @return 值
     */
    DataPicker<String> onSetupSyncServerPicker();

    /**
     * 设置同步间隔
     *
     * @return 秒值
     */
    int onSetupSyncIntervalInSec();

    /**
     * 配置同步的等待超时
     */
    default int onSetupSyncTimeoutInSec() {
        return 10;
    }

    @AllArgsConstructor
    class StdImpl implements ISyncClientConfig {

        private String syncServerUrl;
        private int syncIntervalInSec;

        @Override
        public DataPicker<String> onSetupSyncServerPicker() {
            return new DataPickerSimpleImpl<>(syncServerUrl);
        }

        @Override
        public int onSetupSyncIntervalInSec() {
            return syncIntervalInSec;
        }
    }

}
