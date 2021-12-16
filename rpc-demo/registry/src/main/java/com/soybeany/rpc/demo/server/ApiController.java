package com.soybeany.rpc.demo.server;

import com.soybeany.rpc.core.api.ServiceSyncer;
import com.soybeany.sync.core.model.SyncDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * todo 改为在业务层
 *
 * @author Soybeany
 * @date 2021/10/26
 */
@RestController
@RequestMapping("/bd-api")
public class ApiController {

    @Autowired
    private ServiceSyncer serviceSyncer;

    /**
     * 数据同步
     */
    @PostMapping("/sync")
    public SyncDTO sync(HttpServletRequest request) {
        return serviceSyncer.sync(request);
    }

}
