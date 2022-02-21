package com.soybeany.sync.client.api;

/**
 * @author Soybeany
 * @date 2022/1/22
 */
public interface ISystemConfig {

    /**
     * 配置服务所在的系统，系统间的服务与数据都是隔离的
     */
    default String onSetupSystem() {
        return null;
    }

}
