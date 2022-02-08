package com.demo;

import com.soybeany.rpc.provider.BaseRpcRegistrySyncerImpl;
import com.soybeany.sync.core.picker.DataPicker;
import com.soybeany.sync.core.picker.DataPickerSimpleImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Set;


/**
 * @author Soybeany
 * @date 2021/10/28
 */
@Slf4j
@Component
public class RegistrySyncerImpl extends BaseRpcRegistrySyncerImpl {

    @Override
    protected String onSetupTag() {
        return "oq";
    }

    @Override
    public void onSetupPkgPathToScan(Set<String> paths) {
        paths.add("com.demo");
    }

    @Override
    public DataPicker<String> onSetupSyncServerPicker() {
        return new DataPickerSimpleImpl<>("http://localhost:8080/bd-api/sync");
    }

    @Override
    public int onSetupSyncIntervalInSec() {
        return 3;
    }

    @Override
    protected String onSetupInvokeUrl(String ip) {
        return getUrl(false, ip, 8083, "", Constants.PATH_RPC, "");
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
