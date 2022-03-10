package com.soybeany.sync.client.model;

import lombok.Data;
import org.springframework.context.ApplicationContext;

/**
 * @author Soybeany
 * @date 2022/1/20
 */
@Data
public class SyncClientInfo {

    /**
     * 应用上下文
     */
    private final ApplicationContext appContext;

    /**
     * 当前使用的业务系统
     */
    private final String system;

    /**
     * 当前服务的版本
     */
    private final String version;

    /**
     * 同步间隔(单位：秒)
     */
    private final int syncIntervalSec;

    /**
     * 同步超时(单位：秒)
     */
    private final int syncTimeoutSec;

}
