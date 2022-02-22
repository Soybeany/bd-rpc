package com.demo.broker.impl;

import com.soybeany.mq.core.api.IMqReceiptHandler;
import com.soybeany.mq.core.model.MqReceiptInfo;
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
    public void onSuccess(String ip, Collection<MqReceiptInfo> info) {
        log.info("接收到“" + ip + "”执行" + info + "成功的回执");
    }

    @Override
    public void onException(String ip, Collection<MqReceiptInfo.WithE> info) {
        log.warn("接收到“" + ip + "”执行" + info + "失败的回执");
    }
}
