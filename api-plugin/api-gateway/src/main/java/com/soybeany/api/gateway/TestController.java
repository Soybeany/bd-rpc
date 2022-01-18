package com.soybeany.api.gateway;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Soybeany
 * @date 2022/1/18
 */
@RestController
public class TestController {

    @RequestMapping("/**")
    public String what(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ReverseProxyUtils.start(request, response);
        return "success";
    }


}
