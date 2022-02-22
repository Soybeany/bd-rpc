package com.demo.broker.impl;

import com.soybeany.mq.core.api.IMqReceiptHandler;
import com.soybeany.mq.core.model.MqTopicInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Soybeany
 * @date 2022/2/22
 */
@Slf4j
@Component
public class MqReceiptHandlerImpl implements IMqReceiptHandler {
    @Override
    public void onSuccess(String ip, MqTopicInfo info) {
        log.info("接收到" + ip + "执行“" + info.getTopic() + ":" + info.getStamp() + "”成功的回执");
    }
}
