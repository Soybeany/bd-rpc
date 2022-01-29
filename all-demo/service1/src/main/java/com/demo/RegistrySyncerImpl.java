package com.demo;

import com.soybeany.mq.core.plugin.MqRegistryPlugin;
import com.soybeany.rpc.consumer.BaseRpcRegistrySyncerImpl;
import com.soybeany.rpc.core.model.ServerInfo;
import com.soybeany.sync.core.api.IClientPlugin;
import com.soybeany.sync.core.picker.DataPicker;
import com.soybeany.sync.core.picker.DataPickerSimpleImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Set;

/**
 * @author Soybeany
 * @date 2021/10/28
 */
@Slf4j
@Component
public class RegistrySyncerImpl extends BaseRpcRegistrySyncerImpl {

    @Override
    protected DataPicker<ServerInfo> onGetNewServerPicker(String serviceId) {
        return new DataPickerSimpleImpl<>();
    }

    @Override
    protected void onSetupPlugins(List<IClientPlugin<?, ?>> plugins) {
        super.onSetupPlugins(plugins);
        plugins.add(new MqRegistryPlugin());
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

    @PostConstruct
    private void onInit() {
        start();
    }

    @PreDestroy
    private void onDestroy() {
        stop();
    }

}
