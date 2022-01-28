package com.demo;

import com.soybeany.mq.core.api.IMqMsgHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Soybeany
 * @date 2022/1/20
 */
@Slf4j
@Component
public class MsgHandler1 implements IMqMsgHandler {
    @Override
    public String onSetupTopic() {
        return "1";
    }

    @Override
    public void onHandle(String msg) {
        log.info("1收到:" + msg);
    }
}
