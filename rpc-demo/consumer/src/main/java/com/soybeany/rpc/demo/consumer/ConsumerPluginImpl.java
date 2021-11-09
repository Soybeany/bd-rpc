package com.soybeany.rpc.demo.consumer;

import com.soybeany.rpc.consumer.BaseRpcConsumerPlugin;
import com.soybeany.sync.core.model.Context;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Soybeany
 * @date 2021/10/28
 */
@Log
@Component
public class ConsumerPluginImpl extends BaseRpcConsumerPlugin {

    @Override
    public synchronized void onHandleSync(Context ctx, Map<String, String> param) {
        super.onHandleSync(ctx, param);
        log.info(param.toString());
    }

    @Override
    protected String onSetupScanPkg() {
        return "com.soybeany";
    }

}
