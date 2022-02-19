package com.soybeany.sync.client.api;

import com.soybeany.sync.client.model.SyncState;

import java.util.List;

/**
 * @author Soybeany
 * @date 2022/2/19
 */
public interface ISyncExceptionWatcher {

    /**
     * 同步过程中出现异常时的回调
     *
     * @param plugins 受影响/源发的插件
     * @param uid     用于关联输入/输出的唯一标识
     * @param state   所处的同步状态
     * @param e       异常
     */
    void onSyncException(List<IClientPlugin<Object, Object>> plugins, String uid, SyncState state, Exception e) throws Exception;

}
