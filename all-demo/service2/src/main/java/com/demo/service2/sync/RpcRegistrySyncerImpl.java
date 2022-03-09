package com.demo.service2.sync;

import com.demo.model.Constants;
import com.soybeany.rpc.core.model.RpcServerInfo;
import com.soybeany.rpc.unit.BaseRpcUnitRegistrySyncerImpl;
import com.soybeany.sync.client.picker.DataPicker;
import com.soybeany.sync.client.picker.DataPickerSimpleImpl;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;


/**
 * @author Soybeany
 * @date 2021/10/28
 */
@Slf4j
public class RpcRegistrySyncerImpl extends BaseRpcUnitRegistrySyncerImpl {

    @Override
    protected String onSetupGroup() {
        return "cz";
    }

    @Override
    protected DataPicker<RpcServerInfo> onGetNewServerPicker(String serviceId) {
        return new DataPickerSimpleImpl<>();
    }

    @Override
    protected void onSetupApiPkgToScan(Set<String> paths) {
    }

    @Override
    public void onSetupImplPkgToScan(Set<String> paths) {
        paths.add("com.demo.service2.impl");
    }

    @Override
    public DataPicker<String> onSetupSyncServerPicker() {
        return new DataPickerSimpleImpl<>("http://localhost:8080/bd-api/sync");
    }

    @Override
    public int onSetupSyncIntervalSec() {
        return 3;
    }

    @Override
    protected String onSetupInvokeUrl(String ip) {
        return getUrl(false, ip, 8081, "", Constants.PATH_RPC, "");
    }

}
