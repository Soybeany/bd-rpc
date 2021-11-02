package com.soybeany.rpc.provider;

import com.soybeany.rpc.core.model.MethodInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.soybeany.rpc.core.model.BdRpcConstants.KEY_METHOD_INFO;
import static com.soybeany.rpc.core.model.BdRpcConstants.PATH;
import static com.soybeany.sync.core.util.RequestUtils.GSON;

/**
 * @author Soybeany
 * @date 2021/10/30
 */
@RestController
class RpcProviderController {

    @Autowired
    private BaseRpcProviderPlugin plugin;

    @PostMapping(PATH)
    String bdRpc(HttpServletRequest request, HttpServletResponse response) {
        try {
            String param = request.getParameter(KEY_METHOD_INFO);
            Object result = plugin.invoke(GSON.fromJson(param, MethodInfo.class));
            return GSON.toJson(result);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return "异常:" + e.getMessage();
        }
    }

}
