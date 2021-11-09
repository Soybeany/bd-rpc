package com.soybeany.rpc.demo.provider;

import com.soybeany.rpc.provider.BaseRpcProviderPlugin;
import com.soybeany.rpc.provider.ring.RingDataDAO;
import com.soybeany.rpc.provider.ring.RingDataProvider;
import com.soybeany.sync.core.model.SyncSender;
import com.soybeany.util.file.BdFileUtils;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.context.WebServerInitializedEvent;
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

    @Value("${server.servlet.context-path}")
    private String contextPath;
    private int port = -1;

    private final CountDownLatch latch = new CountDownLatch(1);

    @Override
    protected String onSetupScanPkg() {
        return "com.soybeany";
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
        return contextPath;
    }

    @Override
    protected RingDataProvider<String> onSetupAuthorizationProvider() {
        return new RingDataProvider.Builder<>(BdFileUtils::getUuid, new RingDataDAO.MemImpl<>(), 10 * 1000).build();
    }

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        port = event.getWebServer().getPort();
        latch.countDown();
    }

}
