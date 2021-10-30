package com.soybeany.rpc.demo.consumer;

import com.soybeany.rpc.demo.provider.TestParam;
import com.soybeany.rpc.utl.ServiceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Soybeany
 * @date 2021/10/30
 */
@RestController
public class TestController {

    @Autowired
    private ServiceProvider serviceProvider;

    @GetMapping("/test")
    public String test() {
        ITestService service = serviceProvider.get(ITestService.class);
        TestParam param = new TestParam(3, "success");
        return service.getValue(param).getValue();
    }

}
