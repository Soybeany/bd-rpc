package com.soybeany.sync.core.model;

/**
 * @author Soybeany
 * @date 2022/2/10
 */
public enum SyncState {
    // 同步前
    BEFORE,
    // 同步中
    SYNC,
    // 接收到同步数据
    RECEIVE,
    // 同步后
    AFTER,
}
