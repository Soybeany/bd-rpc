package com.demo;

import com.soybeany.mq.core.plugin.MqRegistryPlugin;
import com.soybeany.rpc.consumer.BaseRpcRegistrySyncerImpl;
import com.soybeany.rpc.core.model.RpcServerInfo;
import com.soybeany.rpc.provider.IRpcServiceExecutor;
import com.soybeany.rpc.provider.RpcProviderPlugin;
import com.soybeany.sync.core.api.IClientPlugin;
import com.soybeany.sync.core.model.SyncDTO;
import com.soybeany.sync.core.picker.DataPicker;
import com.soybeany.sync.core.picker.DataPickerFiltersImpl;
import com.soybeany.sync.core.picker.DataPickerSimpleImpl;
import com.soybeany.sync.core.picker.filter.DataPickerFuseFilter;
import com.soybeany.sync.core.util.NetUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Soybeany
 * @date 2021/10/28
 */
@Slf4j
@Component
public class RegistrySyncerImpl extends BaseRpcRegistrySyncerImpl implements IRpcServiceExecutor {

    private RpcProviderPlugin plugin;

    @Override
    protected DataPicker<RpcServerInfo> onGetNewServerPicker(String serviceId) {
        return new DataPickerFiltersImpl<>(Collections.singletonList(
                new DataPickerFuseFilter<>(10, 0.5f, 5)
        ));
    }

    @Override
    protected void onSetupPlugins(List<IClientPlugin<?, ?>> plugins) {
        super.onSetupPlugins(plugins);
        plugins.add(new MqRegistryPlugin());
        Set<String> paths = new HashSet<>();
        paths.add("com.demo");
        plugin = new RpcProviderPlugin(onSetupSystem(), onSetupVersion(), "cz", appContext, getInvokeUrl(), paths);
        plugins.add(plugin);
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

    @Override
    public SyncDTO execute(HttpServletRequest request, HttpServletResponse response) {
        return plugin.execute(request, response);
    }

    private String getInvokeUrl() {
        return getUrl(false, NetUtils.getLocalIpAddress(), 8082, "", "/bd-rpc/invoke", "");
    }

}
