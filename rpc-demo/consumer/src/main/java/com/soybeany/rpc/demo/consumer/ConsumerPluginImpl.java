package com.soybeany.rpc.demo.consumer;

import com.soybeany.rpc.consumer.BaseRpcConsumerPlugin;
import com.soybeany.rpc.core.model.ServerInfo;
import com.soybeany.sync.core.picker.DataPicker;
import com.soybeany.sync.core.picker.DataPickerSimpleImpl;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

/**
 * @author Soybeany
 * @date 2021/10/28
 */
@Log
@Component
public class ConsumerPluginImpl extends BaseRpcConsumerPlugin {

    @Override
    protected String onSetupPkgToScan() {
        return "com.soybeany.rpc.demo.consumer";
    }

    @Override
    protected DataPicker<ServerInfo> onGetNewServerInfoPicker() {
        return new DataPickerSimpleImpl<>();
    }
}
