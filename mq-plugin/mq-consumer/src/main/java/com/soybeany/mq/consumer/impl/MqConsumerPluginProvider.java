package com.soybeany.mq.consumer.impl;

import com.soybeany.mq.consumer.api.IMqExceptionHandler;
import com.soybeany.mq.consumer.api.IMqMsgHandler;
import com.soybeany.mq.consumer.api.ITopicInfoRepository;
import com.soybeany.mq.consumer.plugin.MqConsumerPlugin;
import com.soybeany.rpc.client.api.IRpcOtherPluginsProvider;
import com.soybeany.sync.client.api.IClientPlugin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author Soybeany
 * @date 2022/3/9
 */
@Slf4j
@SuppressWarnings("SpringJavaAutowiredMembersInspection")
public class MqConsumerPluginProvider implements IRpcOtherPluginsProvider {

    private static final ThreadLocal<MqConsumerPluginInfo> INFO_LOCAL = new ThreadLocal<>();

    @Autowired
    private List<IMqMsgHandler<?>> handlers;

    public static void setInfo(MqConsumerPluginInfo info) {
        INFO_LOCAL.set(info);
    }

    @Override
    public List<IClientPlugin<?, ?>> onSetupPlugins() {
        MqConsumerPluginInfo info = Optional.ofNullable(INFO_LOCAL.get())
                .orElseThrow(() -> new RuntimeException("还没有在主线程中配置MqConsumerPlugin"));
        INFO_LOCAL.remove();
        return Collections.singletonList(
                new MqConsumerPlugin(info.pullIntervalSec, handlers, info.repository, info.exceptionHandler, info.enableReceipt)
        );
    }

    // ***********************内部类****************************

    @RequiredArgsConstructor
    public static class MqConsumerPluginInfo {
        private final int pullIntervalSec;
        private final ITopicInfoRepository repository;
        private final IMqExceptionHandler exceptionHandler;
        private final boolean enableReceipt;
    }

}
