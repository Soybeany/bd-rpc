package com.soybeany.rpc.demo.consumer;

import com.soybeany.rpc.core.exception.RpcPluginException;
import com.soybeany.rpc.core.utl.ServiceProvider;
import com.soybeany.rpc.demo.provider.TestParam;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

/**
 * @author Soybeany
 * @date 2021/10/30
 */
@Log
@RestController
public class TestController {

    @Autowired
    private ServiceProvider serviceProvider;

    @GetMapping("/test")
    public String test() {
        try {
            ITestService service = serviceProvider.get(ITestService.class);
            TestParam param = new TestParam(3, "success");
            return service.getValue(Collections.singletonList(param)).get(0).getValue();
        } catch (RpcPluginException e) {
            String message = e.getMessage();
            log.warning(message);
            return "exception:" + message;
        }
    }

}
