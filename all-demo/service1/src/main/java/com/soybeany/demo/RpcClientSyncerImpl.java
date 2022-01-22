package com.soybeany.demo;

import com.soybeany.mq.core.api.IMqClientInputRListener;
import com.soybeany.mq.core.plugin.MqClientPluginR;
import com.soybeany.rpc.consumer.BaseRpcClientSyncerImpl;
import com.soybeany.rpc.core.model.ServerInfo;
import com.soybeany.sync.core.api.IClientPlugin;
import com.soybeany.sync.core.picker.DataPicker;
import com.soybeany.sync.core.picker.DataPickerSimpleImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

/**
 * @author Soybeany
 * @date 2021/10/28
 */
@Slf4j
@Component
public class RpcClientSyncerImpl extends BaseRpcClientSyncerImpl {

    @Autowired
    private IMqClientInputRListener listener;

    @Override
    protected void onSetupPlugins(List<IClientPlugin<?, ?>> plugins) {
        super.onSetupPlugins(plugins);
        plugins.add(new MqClientPluginR(onSetupSystem(), listener));
    }

    @Override
    protected DataPicker<ServerInfo> onGetNewServerPicker(String serviceId) {
        return new DataPickerSimpleImpl<>();
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

    @PostConstruct
    private void onInit() {
        start();
    }

    @PreDestroy
    private void onDestroy() {
        stop();
    }

}
