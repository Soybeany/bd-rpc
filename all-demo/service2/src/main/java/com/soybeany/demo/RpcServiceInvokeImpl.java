package com.soybeany.demo;

import com.soybeany.rpc.provider.BaseRpcServiceInvokeImpl;
import com.soybeany.sync.core.picker.DataPicker;
import com.soybeany.sync.core.picker.DataPickerSimpleImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static com.soybeany.demo.model.Constants.PATH_RPC;


/**
 * @author Soybeany
 * @date 2021/10/28
 */
@Slf4j
@Component
public class RpcServiceInvokeImpl extends BaseRpcServiceInvokeImpl {

    @Override
    protected String onSetupTag() {
        return "cz";
    }

    @Override
    public String[] onSetupPkgPathToScan() {
        return new String[]{"com.soybeany.demo"};
    }

    @Override
    public DataPicker<String> onGetSyncServerPicker() {
        return new DataPickerSimpleImpl<>("http://localhost:8080/bd-api/rpcSync");
    }

    @Override
    public int onSetupSyncIntervalInSec() {
        return 3;
    }

    @Override
    protected String onSetupInvokeUrl(String ip) {
        return getUrl(false, ip, 8081, "", PATH_RPC, "");
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
