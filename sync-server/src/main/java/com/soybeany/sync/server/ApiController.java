package com.soybeany.sync.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Soybeany
 * @date 2021/10/26
 */
@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private MainService mainService;

    /**
     * 数据同步
     */
    @PostMapping("/sync")
    public Map<String, String> sync(HttpServletRequest request) {
        return mainService.sync(request);
    }

}
