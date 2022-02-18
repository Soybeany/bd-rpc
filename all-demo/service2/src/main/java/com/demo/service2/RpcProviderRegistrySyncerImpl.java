package com.demo.service2;

import com.soybeany.mq.client.plugin.MqClientRegistryPlugin;
import com.soybeany.rpc.consumer.api.IRpcBatchInvoker;
import com.soybeany.rpc.consumer.api.IRpcServiceProxy;
import com.soybeany.rpc.consumer.plugin.RpcConsumerPlugin;
import com.soybeany.rpc.core.exception.RpcPluginException;
import com.soybeany.rpc.provider.BaseRpcProviderRegistrySyncerImpl;
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

import static com.demo.service2.model.Constants.PATH_RPC;


/**
 * @author Soybeany
 * @date 2021/10/28
 */
@Slf4j
@Component
public class RpcProviderRegistrySyncerImpl extends BaseRpcProviderRegistrySyncerImpl implements IRpcServiceProxy {

    private static final Set<String> PKG_TO_SCAN = Collections.singleton("com.demo");

    @Autowired
    private ApplicationContext appContext;

    private RpcConsumerPlugin plugin;

    @Override
    protected String onSetupGroup() {
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
                null
        );
        plugins.add(plugin);
        plugins.add(new MqClientRegistryPlugin());
    }

    @Override
    public <T> T get(Class<T> interfaceClass) throws RpcPluginException {
        return plugin.get(interfaceClass);
    }

    @Override
    public <T> IRpcBatchInvoker<T> getBatch(Class<?> interfaceClass, String methodId) {
        return plugin.getBatch(interfaceClass, methodId);
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
