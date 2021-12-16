package com.soybeany.sync.core.api;


import com.soybeany.sync.core.exception.SyncException;

import java.util.Map;

/**
 * @author Soybeany
 * @date 2021/10/26
 */
public interface IServerPlugin extends IBasePlugin {

    /**
     * 处理同步的回调(必须先配置{@link #onSetupSyncTagToHandle})
     *
     * @param param  入参
     * @param result 数据
     */
    void onHandleSync(Map<String, String> param, Map<String, String> result) throws SyncException;

}
