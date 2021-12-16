package com.soybeany.sync.core.api;

import com.soybeany.sync.core.picker.DataPicker;
import com.soybeany.sync.core.picker.DataPickerSimpleImpl;
import lombok.AllArgsConstructor;

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
    DataPicker<String> onGetSyncServerPicker();

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
        public DataPicker<String> onGetSyncServerPicker() {
            return new DataPickerSimpleImpl<>(syncServerUrl);
        }

        @Override
        public int onSetupSyncIntervalInSec() {
            return syncIntervalInSec;
        }
    }

}
