package com.soybeany.sync.core.api;

/**
 * @author Soybeany
 * @date 2022/1/22
 */
public interface ISystemConfig {

    default String onSetupSystem() {
        return null;
    }

}
