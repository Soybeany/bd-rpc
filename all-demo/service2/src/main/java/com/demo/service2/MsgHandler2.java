package com.demo.service2;

import com.soybeany.mq.consumer.api.IMqMsgHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Soybeany
 * @date 2022/1/20
 */
@Slf4j
@Component
public class MsgHandler2 implements IMqMsgHandler {
    @Override
    public String onSetupTopic() {
        return "2";
    }

    @Override
    public void onHandle(String msg) {
        log.info("2收到:" + msg);
    }
}
