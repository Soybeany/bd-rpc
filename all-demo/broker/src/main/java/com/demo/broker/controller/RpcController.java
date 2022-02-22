package com.demo.broker.controller;

import com.demo.model.Constants;
import com.soybeany.rpc.provider.api.IRpcServiceExecutor;
import com.soybeany.sync.core.model.SyncDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Soybeany
 * @date 2022/2/22
 */
@RestController
public class RpcController {

    @Autowired
    private IRpcServiceExecutor executor;

    @PostMapping(Constants.PATH_RPC)
    SyncDTO bdRpc(HttpServletRequest request, HttpServletResponse response) {
        return executor.execute(request, response);
    }

}
