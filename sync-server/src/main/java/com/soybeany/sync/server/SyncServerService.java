package com.soybeany.sync.server;

import com.soybeany.sync.core.api.IServerPlugin;
import com.soybeany.sync.core.exception.SyncException;
import com.soybeany.sync.core.model.SyncDTO;
import com.soybeany.sync.core.util.TagUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author Soybeany
 * @date 2021/10/26
 */
public class SyncServerService {

    private final List<IServerPlugin> allPlugins;

    public SyncServerService(IServerPlugin... plugins) {
        this.allPlugins = Arrays.asList(plugins);
        Collections.sort(allPlugins);
    }

    public SyncDTO sync(HttpServletRequest request) {
        try {
            Map<String, String> result = onSync(request);
            return SyncDTO.norm(result);
        } catch (SyncException e) {
            return SyncDTO.error(e);
        }
    }

    // ***********************内部方法****************************

    private Map<String, String> onSync(HttpServletRequest request) throws SyncException {
        Map<String, String> result = new HashMap<>();
        Map<String, Map<String, String>> paramMap = TagUtils.split(getParam(request));
        for (IServerPlugin plugin : allPlugins) {
            String tag = plugin.onSetupSyncTagToHandle();
            Map<String, String> tagParam = paramMap.get(tag);
            if (null == tagParam) {
                continue;
            }
            Map<String, String> tmpResult = new HashMap<>();
            plugin.onHandleSync(tagParam, tmpResult);
            tmpResult.forEach((k, v) -> result.put(TagUtils.addTag(tag, k), v));
        }
        return result;
    }

    private Map<String, String> getParam(HttpServletRequest request) {
        Map<String, String> param = new HashMap<>();
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            param.put(name, request.getParameter(name));
        }
        return param;
    }

}
