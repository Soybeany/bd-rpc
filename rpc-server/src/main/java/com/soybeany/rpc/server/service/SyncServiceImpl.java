package com.soybeany.rpc.server.service;

import com.soybeany.rpc.core.INormServicePlugin;
import com.soybeany.rpc.core.IPostTreatServicePlugin;
import com.soybeany.rpc.core.IPreTreatServicePlugin;
import com.soybeany.rpc.core.IServicePlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author Soybeany
 * @date 2021/10/26
 */
@Service
class SyncServiceImpl implements SyncService {

    @Autowired
    private List<IServicePlugin> allPlugins;

    private final List<IPreTreatServicePlugin> preTreatPlugins = new LinkedList<>();
    private final Map<String, List<INormServicePlugin>> normPlugins = new HashMap<>();
    private final List<IPostTreatServicePlugin> postTreatPlugins = new LinkedList<>();

    @Override
    public Map<String, String> sync(HttpServletRequest request) {
        Map<String, String> result = new HashMap<>();
        Map<String, String[]> param = request.getParameterMap();
        preTreatPlugins.forEach(plugin -> plugin.onHandle(request, param, result));
        for (Map.Entry<String, Map<String, String[]>> entry : sortParams(param).entrySet()) {
            String tag = entry.getKey();
            Map<String, String> tmpResult = new HashMap<>();
            Optional.ofNullable(normPlugins.get(tag))
                    .ifPresent(plugins -> plugins.forEach(plugin -> plugin.onHandle(request, entry.getValue(), tmpResult)));
            tmpResult.forEach((k, v) -> result.put(tag + IServicePlugin.SEPARATOR + k, v));
        }
        postTreatPlugins.forEach(plugin -> plugin.onHandle(request, param, result));
        return result;
    }

    // ***********************内部方法****************************

    @PostConstruct
    private void onInit() {
        allPlugins.stream().sorted().forEach(this::sortPlugins);
    }

    private Map<String, Map<String, String[]>> sortParams(Map<String, String[]> param) {
        Map<String, Map<String, String[]>> result = new HashMap<>();
        for (Map.Entry<String, String[]> entry : param.entrySet()) {
            String key = entry.getKey();
            int separatorIndex = key.indexOf(IServicePlugin.SEPARATOR);
            if (separatorIndex == -1) {
                continue;
            }
            String tag = key.substring(0, separatorIndex);
            String restKey = key.substring(separatorIndex + IServicePlugin.SEPARATOR.length());
            result.computeIfAbsent(tag, k -> new HashMap<>()).put(restKey, entry.getValue());
        }
        return result;
    }

    private void sortPlugins(IServicePlugin plugin) {
        if (plugin instanceof IPreTreatServicePlugin) {
            preTreatPlugins.add((IPreTreatServicePlugin) plugin);
        } else if (plugin instanceof INormServicePlugin) {
            INormServicePlugin nPlugin = (INormServicePlugin) plugin;
            normPlugins.computeIfAbsent(nPlugin.onSetupHandleTag(), k -> new LinkedList<>()).add(nPlugin);
        } else if (plugin instanceof IPostTreatServicePlugin) {
            postTreatPlugins.add((IPostTreatServicePlugin) plugin);
        } else {
            throw new RuntimeException("请实现IServicePlugin的子类接口");
        }
    }

}
