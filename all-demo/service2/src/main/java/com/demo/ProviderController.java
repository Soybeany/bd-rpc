package com.demo;

import com.demo.model.Constants;
import com.soybeany.rpc.core.api.IRpcServiceExecutor;
import com.soybeany.sync.core.model.SyncDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Soybeany
 * @date 2021/10/30
 */
@RestController
class ProviderController {

    @Autowired
    private IRpcServiceExecutor invoker;

    @PostMapping(Constants.PATH_RPC)
    SyncDTO bdRpc(HttpServletRequest request, HttpServletResponse response) {
        return invoker.execute(request, response);
    }

}
