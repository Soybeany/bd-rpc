package com.soybeany.rpc.core.model;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author Soybeany
 * @date 2021/12/16
 */
public abstract class BaseServiceImpl {

    @PostConstruct
    private void start() {
        onStart();
    }

    @PreDestroy
    private void stop() {
        onStop();
    }

    // ***********************子类实现****************************

    protected void onStart() {
    }

    protected void onStop() {
    }

}
