package com.soybeany.rpc.demo.consumer;

import com.soybeany.rpc.consumer.BaseServiceProxyImpl;
import com.soybeany.rpc.core.model.ServerInfo;
import com.soybeany.sync.core.picker.DataPicker;
import com.soybeany.sync.core.picker.DataPickerSimpleImpl;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

/**
 * @author Soybeany
 * @date 2021/10/28
 */
@Log
@Component
public class ServiceProxyImpl extends BaseServiceProxyImpl {

    @Override
    protected String onSetupSystem() {
        return null;
    }

    @Override
    protected String[] onSetupPkgPathToScan() {
        return new String[]{"com.soybeany.rpc.demo.consumer"};
    }

    @Override
    protected DataPicker<ServerInfo> onGetNewDataPicker(String serviceId) {
        return new DataPickerSimpleImpl<>();
    }

    @Override
    public String onGetSyncServerUrl() {
        return "http://localhost:8080/bd-api/sync";
    }

    @Override
    public int onSetupSyncIntervalInSec() {
        return 3;
    }
}
