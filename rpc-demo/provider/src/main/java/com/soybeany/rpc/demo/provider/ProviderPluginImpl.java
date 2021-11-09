package com.soybeany.rpc.demo.provider;

import com.soybeany.rpc.provider.BaseRpcProviderPlugin;
import com.soybeany.rpc.provider.ring.RingDataDAO;
import com.soybeany.rpc.provider.ring.RingDataProvider;
import com.soybeany.sync.core.model.SyncSender;
import com.soybeany.util.file.BdFileUtils;
import lombok.extern.java.Log;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.boot.web.server.WebServer;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

/**
 * @author Soybeany
 * @date 2021/10/28
 */
@Log
@Component
public class ProviderPluginImpl extends BaseRpcProviderPlugin implements ApplicationListener<WebServerInitializedEvent> {

    private int port = -1;

    private final CountDownLatch latch = new CountDownLatch(1);

    @Override
    protected String onSetupPkgToScan() {
        return "com.soybeany.rpc.demo.provider";
    }

    @Override
    public void onStartup(SyncSender sender) {
        try {
            latch.await();
        } catch (InterruptedException ignore) {
        }
    }

    @Override
    protected int onSetupServerPort() {
        return port;
    }

    @Override
    protected String onSetupServerContextPath() {
        return "";
    }

    @Override
    protected RingDataProvider<String> onSetupAuthorizationProvider() {
        return new RingDataProvider.Builder<>(BdFileUtils::getUuid, new RingDataDAO.MemImpl<>(), 20 * 1000).build();
    }

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        WebServer server = event.getWebServer();
        port = server.getPort();
        latch.countDown();
    }

}
