package com.soybeany.demo;

import com.soybeany.demo.model.TestParam;
import com.soybeany.mq.core.api.IMqMsgSender;
import com.soybeany.mq.core.exception.MqPluginException;
import com.soybeany.mq.core.model.broker.MqProducerMsg;
import com.soybeany.rpc.core.api.IRpcServiceProxy;
import com.soybeany.rpc.core.exception.RpcPluginException;
import com.soybeany.rpc.core.model.ProxySelector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Collections;

/**
 * @author Soybeany
 * @date 2021/10/30
 */
@Slf4j
@RestController
public class TestController {

    @Autowired
    private IRpcServiceProxy serviceProxy;
    @Autowired
    private IMqMsgSender mqMsgSender;

    private ProxySelector<ITestService> service;

    @GetMapping("/test")
    public String test(String tag, String topic) {
        try {
            TestParam param = new TestParam(3, "success");
            String value = service.get(tag).getValue(Collections.singletonList(param)).get(0).getValue();
            mqMsgSender.syncSend(topic, new MqProducerMsg(LocalDateTime.now(), LocalDateTime.now().plusSeconds(20), value));
            return value;
        } catch (RpcPluginException | MqPluginException e) {
            String message = e.getMessage();
            log.warn(message);
            return "exception:" + message;
        } catch (Exception e) {
            e.printStackTrace();
            return "未预料异常:" + e.getMessage();
        }
    }

    @PostConstruct
    private void onInit() {
        service = serviceProxy.getSelector(ITestService.class);
    }

}
