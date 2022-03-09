package com.demo.service1.sync;

import com.soybeany.mq.core.model.MqProducerMsg;
import com.soybeany.mq.producer.api.IMqMsgSender;
import com.soybeany.mq.producer.plugin.MqProducerPlugin;
import com.soybeany.rpc.core.model.RpcServerInfo;
import com.soybeany.rpc.unit.BaseRpcUnitRegistrySyncerImpl;
import com.soybeany.sync.client.api.IClientPlugin;
import com.soybeany.sync.client.picker.DataPicker;
import com.soybeany.sync.client.picker.DataPickerFiltersImpl;
import com.soybeany.sync.client.picker.DataPickerSimpleImpl;
import com.soybeany.sync.client.picker.filter.DataPickerFuseFilter;
import com.soybeany.sync.core.util.NetUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author Soybeany
 * @date 2021/10/28
 */
@Slf4j
@Component
public class RpcRegistrySyncerImpl extends BaseRpcUnitRegistrySyncerImpl implements IMqMsgSender {

    private MqProducerPlugin mqProducerPlugin;

    @Override
    protected String onSetupGroup() {
        return "cz";
    }

    @Override
    protected DataPicker<String> onSetupSyncServerPicker() {
        return new DataPickerSimpleImpl<>("http://localhost:8080/bd-api/sync");
    }

    @Override
    protected int onSetupSyncIntervalSec() {
        return 3;
    }

    @Override
    protected void onSetupApiPkgToScan(Set<String> paths) {
        paths.add("com.demo.service");
    }

    @Override
    protected void onSetupImplPkgToScan(Set<String> paths) {
        paths.add("com.demo.service1.impl");
    }

    @Override
    protected DataPicker<RpcServerInfo> onGetNewServerPicker(String serviceId) {
        return new DataPickerFiltersImpl<>(Collections.singletonList(
                new DataPickerFuseFilter<>(10, 0.5f, 5)
        ));
    }

    @Override
    protected String onSetupInvokeUrl(String ip) {
        return getUrl(false, NetUtils.getLocalIpAddress(), 8082, "", "/bd-rpc/invoke", "");
    }

    @Override
    protected void onSetupPlugins(List<IClientPlugin<?, ?>> plugins) {
        super.onSetupPlugins(plugins);
        plugins.add(mqProducerPlugin = new MqProducerPlugin(this));
    }

    @Override
    public <T extends Serializable> void send(String topic, MqProducerMsg<T> msg) {
        mqProducerPlugin.send(topic, msg);
    }

}
