package com.soybeany.sync.core.api;


import com.soybeany.sync.core.exception.SyncException;

/**
 * @author Soybeany
 * @date 2021/10/26
 */
public interface IServerPlugin<Input, Output> extends IBasePlugin<Input, Output> {

    /**
     * 处理同步的回调(必须先配置{@link #onSetupSyncTagToHandle})
     */
    void onHandleSync(Input input, Output output) throws SyncException;

}
