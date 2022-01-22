package com.soybeany.rpc.demo.server;

import com.soybeany.sync.core.api.IServerSyncer;
import com.soybeany.sync.core.model.SyncDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    @Qualifier("RpcServiceSyncer")
    private IServerSyncer rpcServiceSyncer;

    @Autowired
    @Qualifier("MqServiceSyncer")
    private IServerSyncer mqServiceSyncer;

    @PostMapping("/rpcSync")
    public SyncDTO rpcSync(HttpServletRequest request) {
        return rpcServiceSyncer.sync(request);
    }

    @PostMapping("/mqSync")
    public SyncDTO mqSync(HttpServletRequest request) {
        return mqServiceSyncer.sync(request);
    }

}
