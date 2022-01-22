package com.soybeany.demo;

import com.soybeany.mq.core.api.IMqClientInputRListener;
import com.soybeany.mq.core.client.MqClientPlugin;
import com.soybeany.rpc.provider.BaseRpcRegistrySyncerImplP;
import com.soybeany.sync.core.api.IClientPlugin;
import com.soybeany.sync.core.picker.DataPicker;
import com.soybeany.sync.core.picker.DataPickerSimpleImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

import static com.soybeany.demo.model.Constants.PATH_RPC;


/**
 * @author Soybeany
 * @date 2021/10/28
 */
@Slf4j
@Component
public class RegistrySyncerImplP extends BaseRpcRegistrySyncerImplP {

    @Autowired
    private IMqClientInputRListener listener;

    @Override
    protected String onSetupTag() {
        return "cz";
    }

    @Override
    protected void onSetupPlugins(List<IClientPlugin<?, ?>> plugins) {
        super.onSetupPlugins(plugins);
        plugins.add(new MqClientPlugin(onSetupSystem(), listener));
    }

    @Override
    public String[] onSetupPkgPathToScan() {
        return new String[]{"com.soybeany.demo"};
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
