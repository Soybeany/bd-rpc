package com.soybeany.sync.client.model;

import lombok.Data;

/**
 * @author Soybeany
 * @date 2022/1/20
 */
@Data
public class SyncClientInfo {

    /**
     * 同步间隔(单位：秒)
     */
    private final int syncIntervalInSec;

    /**
     * 同步超时
     */
    private final int syncTimeout;

}
