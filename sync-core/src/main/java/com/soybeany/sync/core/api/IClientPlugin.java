package com.soybeany.sync.core.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Soybeany
 * @date 2021/10/27
 */
public interface IClientPlugin<Input, Output> extends IBasePlugin<Input, Output> {

    Logger LOGGER = LoggerFactory.getLogger(IClientPlugin.class);

    /**
     * 应用启动时的回调
     *
     * @param syncIntervalInSec 同步间隔(单位：秒)
     */
    default void onStartup(int syncIntervalInSec) {
    }

    /**
     * 应用关闭时的回调
     */
    default void onShutdown() {
    }

    /**
     * 处理数据输出（触发心跳时的回调）
     *
     * @param uid    用于关联输入/输出的唯一标识
     * @param output 待传输至服务器的数据
     */
    default void onHandleOutput(String uid, Output output) throws Exception {
    }

    /**
     * 处理数据输入(必须先配置{@link #onSetupSyncTagToHandle})
     *
     * @param uid   用于关联输入/输出的唯一标识
     * @param input 入参
     */
    default void onHandleInput(String uid, Input input) throws Exception {
    }

    /**
     * 处理心跳异常
     *
     * @param uid 用于关联输入/输出的唯一标识
     * @param e   异常
     */
    default void onHandleException(String uid, Exception e) throws Exception {
        LOGGER.warn(e.getMessage());
    }

}
