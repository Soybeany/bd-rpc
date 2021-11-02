package com.soybeany.rpc.demo.consumer;

import com.soybeany.sync.client.ISyncClientConfig;
import org.springframework.stereotype.Component;

/**
 * @author Soybeany
 * @date 2021/11/2
 */
@Component
public class SyncServerConfig implements ISyncClientConfig {
    @Override
    public String onGetSyncServerUrl() {
        return "http://localhost:8080/api/sync";
    }

    @Override
    public int onSetupSyncIntervalInSec() {
        return 3;
    }
}
