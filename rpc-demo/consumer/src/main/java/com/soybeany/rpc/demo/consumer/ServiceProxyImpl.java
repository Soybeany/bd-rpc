package com.soybeany.rpc.demo.consumer;

import com.soybeany.rpc.consumer.BaseServiceProxyImpl;
import com.soybeany.rpc.core.model.ServerInfo;
import com.soybeany.sync.core.picker.DataPicker;
import com.soybeany.sync.core.picker.DataPickerSimpleImpl;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author Soybeany
 * @date 2021/10/28
 */
@Log
@Component
public class ServiceProxyImpl extends BaseServiceProxyImpl {

    @Override
    protected DataPicker<ServerInfo> onGetNewServerPicker(String serviceId) {
        return new DataPickerSimpleImpl<>();
    }

    @Override
    public String onSetupSystem() {
        return null;
    }

    @Override
    public String[] onSetupPkgPathToScan() {
        return new String[]{"com.soybeany.rpc.demo.consumer"};
    }

    @Override
    public DataPicker<String> onGetSyncServerPicker() {
        return new DataPickerSimpleImpl<>("http://localhost:8080/bd-api/sync");
    }

    @Override
    public int onSetupSyncIntervalInSec() {
        return 3;
    }

    @PostConstruct
    private void onInit() {
        start();
    }

    @PreDestroy
    private void onDestroy() {
        stop();
    }

}
