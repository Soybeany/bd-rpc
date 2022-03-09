package com.demo.broker.sync;

import com.demo.model.Constants;
import com.soybeany.rpc.provider.BaseRpcProviderRegistrySyncerImpl;
import com.soybeany.sync.client.picker.DataPicker;
import com.soybeany.sync.client.picker.DataPickerSimpleImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;


/**
 * @author Soybeany
 * @date 2021/10/28
 */
@Slf4j
@Component
public class RpcRegistrySyncerImpl extends BaseRpcProviderRegistrySyncerImpl {

    @Override
    protected String onSetupGroup() {
        return "oq";
    }

    @Override
    public void onSetupImplPkgToScan(Set<String> paths) {
        paths.add("com.demo.broker.impl");
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
        return getUrl(false, ip, 8083, "", Constants.PATH_RPC, "");
    }

}
