package com.soybeany.rpc.server.controller;

import com.soybeany.rpc.server.service.SyncService;
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
    private SyncService syncService;

    @PostMapping("/sync")
    public Map<String, String> sync(HttpServletRequest request) {
        return syncService.sync(request);
    }

}
