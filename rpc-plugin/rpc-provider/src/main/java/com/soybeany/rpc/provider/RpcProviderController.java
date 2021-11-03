package com.soybeany.rpc.provider;

import com.soybeany.rpc.core.model.MethodInfo;
import com.soybeany.rpc.core.model.RpcDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

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
    RpcDTO bdRpc(HttpServletRequest request) {
        try {
            String param = request.getParameter(KEY_METHOD_INFO);
            return RpcDTO.norm(plugin.invoke(GSON.fromJson(param, MethodInfo.class)));
        } catch (Throwable throwable) {
            try {
                return RpcDTO.error(throwable);
            } catch (IOException e2) {
                return RpcDTO.error("异常信息构建失败:" + e2.getMessage());
            }
        }
    }

}
