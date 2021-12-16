package com.soybeany.rpc.demo.server;

import com.soybeany.sync.core.model.SyncDTO;
import com.soybeany.sync.server.SyncServerService;
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

    private final SyncServerService syncServerService = new SyncServerService(new RegistryPluginImpl());

    /**
     * 数据同步
     */
    @PostMapping("/sync")
    public SyncDTO sync(HttpServletRequest request) {
        return syncServerService.sync(request);
    }

}
