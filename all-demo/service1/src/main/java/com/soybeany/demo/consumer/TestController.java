package com.soybeany.demo.consumer;

import com.soybeany.rpc.core.api.IRpcServiceProxy;
import com.soybeany.rpc.core.exception.RpcPluginException;
import com.soybeany.rpc.core.model.ProxySelector;
import com.soybeany.demo.model.TestParam;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Collections;

/**
 * @author Soybeany
 * @date 2021/10/30
 */
@Log
@RestController
public class TestController {

    @Autowired
    private IRpcServiceProxy serviceProxy;

    private ProxySelector<ITestService> service;

    @GetMapping("/test")
    public String test(String tag) {
        try {
            TestParam param = new TestParam(3, "success");
            return service.get(tag).getValue(Collections.singletonList(param)).get(0).getValue();
        } catch (RpcPluginException e) {
            String message = e.getMessage();
            log.warning(message);
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
