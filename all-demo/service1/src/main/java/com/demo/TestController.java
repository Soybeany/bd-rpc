package com.demo;

import com.demo.model.TestParam;
import com.soybeany.mq.core.api.IMqMsgSender;
import com.soybeany.mq.core.exception.MqPluginException;
import com.soybeany.mq.core.model.MqProducerMsg;
import com.soybeany.rpc.core.api.IRpcBatchInvoker;
import com.soybeany.rpc.core.api.IRpcServiceProxy;
import com.soybeany.rpc.core.exception.RpcPluginException;
import com.soybeany.rpc.core.exception.RpcRequestException;
import com.soybeany.rpc.core.model.RpcBatchResult;
import com.soybeany.rpc.core.model.RpcProxySelector;
import com.soybeany.rpc.core.model.RpcServerInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

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

    private RpcProxySelector<ITestService> service;
    private RpcProxySelector<IRpcBatchInvoker<String>> invoker;

    @GetMapping("/test")
    public String test(String group, String topic) {
        try {
            TestParam param = new TestParam(3, "success");
            String value = service.get(group).getValue(Collections.singletonList(param)).get(0).getValue();
            Map<RpcServerInfo, RpcBatchResult<String>> resultMap = invoker.get(group).invoke("b输入");
            mqMsgSender.syncSend(topic, new MqProducerMsg(LocalDateTime.now(), LocalDateTime.now().plusSeconds(20), value));
            return value + "\n" + resultMap.values();
        } catch (RpcPluginException | MqPluginException e) {
            String message = e.getMessage();
            if (e instanceof RpcRequestException) {
                message = message + "(" + ((RpcRequestException) e).getServerInfo().getInvokeUrl() + ")";
            }
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
        invoker = serviceProxy.getBatchSelector(ITestService.class, "batch");
    }

}
