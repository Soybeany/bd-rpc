package com.demo;

import com.soybeany.rpc.consumer.RpcConsumerPlugin;
import com.soybeany.rpc.core.api.IRpcServiceProxy;
import com.soybeany.rpc.core.exception.RpcPluginException;
import com.soybeany.rpc.core.model.ProxySelector;
import com.soybeany.rpc.provider.BaseRpcRegistrySyncerImpl;
import com.soybeany.sync.core.api.IClientPlugin;
import com.soybeany.sync.core.picker.DataPicker;
import com.soybeany.sync.core.picker.DataPickerSimpleImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.demo.model.Constants.PATH_RPC;


/**
 * @author Soybeany
 * @date 2021/10/28
 */
@Slf4j
@Component
public class RegistrySyncerImpl extends BaseRpcRegistrySyncerImpl implements IRpcServiceProxy {

    private static final Set<String> PKG_TO_SCAN = Collections.singleton("com.demo");

    @Autowired
    private ApplicationContext appContext;

    private RpcConsumerPlugin plugin;

    @Override
    protected String onSetupTag() {
        return "cz";
    }

    @Override
    public void onSetupPkgPathToScan(Set<String> paths) {
        paths.addAll(PKG_TO_SCAN);
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

    @Override
    protected void onSetupPlugins(List<IClientPlugin<?, ?>> plugins) {
        super.onSetupPlugins(plugins);
        plugin = new RpcConsumerPlugin(
                onSetupSystem(),
                onSetupVersion(),
                appContext,
                serviceId -> new DataPickerSimpleImpl<>(),
                serviceId -> 5,
                Collections.emptySet()
        );
        plugins.add(plugin);
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
    public <T> T get(Class<T> interfaceClass) throws RpcPluginException {
        return plugin.get(interfaceClass);
    }

    @Override
    public <T> ProxySelector<T> getSelector(Class<T> interfaceClass) throws RpcPluginException {
        return plugin.getSelector(interfaceClass);
    }
}
