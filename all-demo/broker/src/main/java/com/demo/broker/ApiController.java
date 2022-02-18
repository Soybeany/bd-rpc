package com.demo.broker;

import com.soybeany.rpc.provider.api.IRpcServiceExecutor;
import com.soybeany.sync.core.api.IServerSyncer;
import com.soybeany.sync.core.model.SyncDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Soybeany
 * @date 2021/10/26
 */
@RestController
public class ApiController {

    @Autowired
    private IRpcServiceExecutor invoker;

    @Autowired
    private IServerSyncer serverSyncer;

    @PostMapping(Constants.PATH_RPC)
    SyncDTO bdRpc(HttpServletRequest request, HttpServletResponse response) {
        return invoker.execute(request, response);
    }

    @PostMapping("/bd-api/bSync")
    public SyncDTO bSync(HttpServletRequest request) {
        return serverSyncer.sync(request);
    }

}
