package com.soybeany.rpc.plugin;

import com.soybeany.rpc.model.MethodInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.soybeany.sync.core.util.RequestUtils.GSON;

/**
 * @author Soybeany
 * @date 2021/10/30
 */
@RestController
public class TmpController {

    @Autowired(required = false)
    private BaseRpcProviderPlugin plugin;

    @PostMapping(Constants.PATH)
    public String bdRpc(HttpServletRequest request) {
        try {
            String param = request.getParameter(Constants.KEY_METHOD_INFO);
            Object result = plugin.invoke(GSON.fromJson(param, MethodInfo.class));
            return GSON.toJson(result);
        } catch (Exception e) {
            return "异常:" + e.getMessage();
        }
    }

}
