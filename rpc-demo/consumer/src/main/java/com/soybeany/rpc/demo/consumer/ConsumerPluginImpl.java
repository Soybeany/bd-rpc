package com.soybeany.rpc.demo.consumer;

import com.soybeany.rpc.consumer.BaseRpcConsumerPlugin;
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

}
