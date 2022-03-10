package com.demo.service1.controller;

import com.demo.model.Interface;
import com.demo.model.ParamImpl;
import com.demo.model.TestParam;
import com.demo.service.*;
import com.soybeany.mq.core.exception.MqPluginException;
import com.soybeany.mq.core.model.MqProducerMsg;
import com.soybeany.mq.producer.api.IMqMsgSender;
import com.soybeany.rpc.consumer.anno.BdRpcWired;
import com.soybeany.rpc.consumer.api.IRpcBatchInvoker;
import com.soybeany.rpc.consumer.api.IRpcServiceProxy;
import com.soybeany.rpc.consumer.exception.RpcRequestException;
import com.soybeany.rpc.consumer.model.RpcBatchResult;
import com.soybeany.rpc.consumer.model.RpcProxySelector;
import com.soybeany.rpc.core.exception.RpcPluginException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author Soybeany
 * @date 2021/10/30
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private IRpcServiceProxy serviceProxy;
    @Autowired
    private IMqMsgSender mqMsgSender;

    private RpcProxySelector<ISimpleTestService> simpleServiceSelector;
    private IRpcBatchInvoker<IBatchTestService> batchInvoker;
    @BdRpcWired
    private ICacheTestService cacheTestService;
    @BdRpcWired
    private IExceptionTestService exceptionTestService;
    @BdRpcWired
    private IFallbackTestService fallbackTestService;
    @BdRpcWired
    private ILocalService localService;
    @BdRpcWired
    private IObjTestService objTestService;
    @BdRpcWired
    private IShareStorageTestService shareStorageTestService;

    @GetMapping("/group")
    public String group(String group, String input) {
        return wrap(() -> simpleServiceSelector.get(group).getValue(input));
    }

    @GetMapping("/batch")
    public String batch(String input) {
        return wrap(() -> {
            Map<String, RpcBatchResult<?>> result = new HashMap<>();
            batchInvoker.invoke(input).forEach((info, r) -> {
                result.put(info.getInvokeUrl(), r);
            });
            return result.toString();
        });
    }

    @GetMapping("/cache")
    public String cache() {
        return wrap(() -> {
            int v1 = cacheTestService.getValue();
            int v2 = cacheTestService.getValue();
            return (v1 == v2) + "";
        });
    }

    @GetMapping("/exception")
    public String exception() {
        String r1 = wrap(() -> exceptionTestService.getValue());
        String r2 = wrap(() -> exceptionTestService.getValue2());
        String r3 = wrap(() -> exceptionTestService.getValue3());
        return r1 + "\n" + r2 + "\n" + r3;
    }

    @GetMapping("/fuse")
    public String fuse() {
        return wrap(() -> fallbackTestService.getValue());
    }

    @GetMapping("/local")
    public String local() {
        return wrap(() -> localService.getValue());
    }

    @GetMapping("/obj")
    public String obj() {
        return wrap(() -> {
            TestParam param1 = new TestParam(1, "ok");
            String r1 = objTestService.getValue(Collections.singletonList(param1)).get(0).getValue();
            Interface param2 = new ParamImpl("input");
            String r2 = objTestService.getValue(param2).getValue();
            return r1 + "\n" + r2;
        });
    }

    @GetMapping("/share")
    public String share() {
        return wrap(() -> {
            int v1 = shareStorageTestService.getValue();
            int v2 = shareStorageTestService.getValue2();
            return (v1 == v2) + "";
        });
    }

    @PostMapping("/sendMq")
    public String sendMq(String value) {
        return wrap(() -> {
            mqMsgSender.send("1", new MqProducerMsg<>(LocalDateTime.now(), LocalDateTime.now().plusSeconds(20), value));
            mqMsgSender.send("2", new MqProducerMsg<>(LocalDateTime.now(), LocalDateTime.now().plusSeconds(20), value));
            return "已发送";
        });
    }

    @PostConstruct
    private void onInit() {
        simpleServiceSelector = serviceProxy.getSelector(ISimpleTestService.class);
        batchInvoker = serviceProxy.getBatch(IBatchTestService.class, "batch");
    }

    // ***********************内部方法****************************

    private String wrap(Callable<String> callable) {
        try {
            return callable.call();
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

}
