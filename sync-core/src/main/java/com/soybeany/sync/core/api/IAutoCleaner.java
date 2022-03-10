package com.soybeany.sync.core.api;

/**
 * @author Soybeany
 * @date 2022/1/20
 */
public interface IAutoCleaner {

    void startAutoClean(long validPeriodInSec);

    void shutdownAutoClean();

}
