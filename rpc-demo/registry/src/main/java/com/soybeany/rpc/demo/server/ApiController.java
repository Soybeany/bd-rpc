package com.soybeany.rpc.demo.server;

import com.soybeany.sync.core.api.IServiceSyncer;
import com.soybeany.sync.core.model.SyncDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Soybeany
 * @date 2021/10/26
 */
@RestController
@RequestMapping("/bd-api")
public class ApiController {

    @Autowired
    private IServiceSyncer serviceSyncer;

    /**
     * 数据同步
     */
    @PostMapping("/sync")
    public SyncDTO sync(HttpServletRequest request) {
        return serviceSyncer.sync(request);
    }

}
