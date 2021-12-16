package com.soybeany.rpc.demo.provider;

import com.soybeany.rpc.provider.BaseServiceInvokeImpl;
import com.soybeany.sync.core.picker.DataPicker;
import com.soybeany.sync.core.picker.DataPickerSimpleImpl;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

import static com.soybeany.rpc.demo.model.Constants.PATH_RPC;


/**
 * @author Soybeany
 * @date 2021/10/28
 */
@Log
@Component
public class ServiceInvokeImpl extends BaseServiceInvokeImpl {

    @Override
    protected String onSetupSystem() {
        return null;
    }

    @Override
    protected String[] onSetupPkgPathToScan() {
        return new String[]{"com.soybeany.rpc.demo.provider"};
    }

    @Override
    public DataPicker<String> onGetSyncServerPicker() {
        return new DataPickerSimpleImpl<>("http://localhost:8080/bd-api/sync");
    }

    @Override
    public int onSetupSyncIntervalInSec() {
        return 3;
    }

    @Override
    protected String onSetupServerSyncUrl(String ip) {
        return getUrl(false, ip, 8081, "", PATH_RPC, "");
    }

}
