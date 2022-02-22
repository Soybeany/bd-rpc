package com.demo.broker.impl;

import com.soybeany.mq.core.api.IMqReceiptHandler;
import com.soybeany.mq.core.model.MqTopicExceptionInfo;
import com.soybeany.mq.core.model.MqTopicInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @author Soybeany
 * @date 2022/2/22
 */
@Slf4j
@Component
public class MqReceiptHandlerImpl implements IMqReceiptHandler {
    @Override
    public void onSuccess(String ip, Collection<MqTopicInfo> topics) {
        log.info("接收到“" + ip + "”执行" + topics + "成功的回执");
    }

    @Override
    public void onException(String ip, Collection<MqTopicExceptionInfo> topics) {
        log.warn("接收到“" + ip + "”执行" + topics + "失败的回执");
    }
}
