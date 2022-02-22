package com.demo.service2.handler;

import com.soybeany.mq.consumer.api.IMqMsgHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Soybeany
 * @date 2022/1/20
 */
@Slf4j
@Component
public class MsgHandler1 implements IMqMsgHandler<String> {
    @Override
    public String onSetupTopic() {
        return "1";
    }

    @Override
    public void onHandle(List<String> messages) {
        log.info("1收到:" + messages);
    }
}
