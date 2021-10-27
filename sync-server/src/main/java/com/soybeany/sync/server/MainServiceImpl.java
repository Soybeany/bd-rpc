package com.soybeany.sync.server;

import com.soybeany.sync.core.api.IServerPlugin;
import com.soybeany.sync.core.model.Context;
import com.soybeany.sync.core.util.TagUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Soybeany
 * @date 2021/10/26
 */
@Service
class MainServiceImpl implements MainService {

    @Autowired
    private List<IServerPlugin> allPlugins;

    @Override
    public Map<String, String> sync(HttpServletRequest request) {
        Context ctx = Context.getNew(request);
        Map<String, String> result = new HashMap<>();
        Map<String, Map<String, String[]>> paramMap = TagUtils.split(request.getParameterMap());
        for (IServerPlugin plugin : allPlugins) {
            Map<String, String> tmpResult = new HashMap<>();
            String tag = plugin.onSetupSyncTagToHandle();
            plugin.onHandleSync(ctx, paramMap.get(tag), tmpResult);
            tmpResult.forEach((k, v) -> result.put(TagUtils.addTag(tag, k), v));
        }
        return result;
    }

    // ***********************内部方法****************************

    @PostConstruct
    private void onInit() {
        Collections.sort(allPlugins);
    }

}
