package com.soybeany.sync.server;

import com.soybeany.sync.core.api.IBasePlugin;
import com.soybeany.sync.core.exception.SyncException;
import com.soybeany.sync.server.api.IServerPlugin;
import com.soybeany.sync.server.api.ISyncExceptionAware;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.soybeany.sync.core.util.NetUtils.GSON;
import static com.soybeany.sync.core.util.NetUtils.getRemoteIpAddress;

/**
 * @author Soybeany
 * @date 2021/10/26
 */
@Slf4j
public class SyncServerService {

    private final List<IServerPlugin<Object, Object>> allPlugins;
    private final ISyncExceptionAware syncExceptionAware;

    public SyncServerService(IServerPlugin<Object, Object>[] plugins, ISyncExceptionAware syncExceptionAware) {
        allPlugins = Arrays.asList(plugins);
        this.syncExceptionAware = syncExceptionAware;
        IBasePlugin.checkPlugins(allPlugins);
        Collections.sort(allPlugins);
    }

    public Map<String, String> sync(HttpServletRequest request) throws SyncException {
        try {
            return onSync(request);
        } catch (Exception e) {
            // 执行异常监控回调
            try {
                syncExceptionAware.onSyncException(allPlugins, e);
            } catch (Exception e2) {
                log.error(e2.getMessage());
            }
            // 抛出异常
            SyncException syncException;
            if (e instanceof SyncException) {
                syncException = (SyncException) e;
            } else {
                syncException = new SyncException(e.getMessage());
            }
            throw syncException;
        }
    }

    // ***********************内部方法****************************

    private Map<String, String> onSync(HttpServletRequest request) throws Exception {
        Map<String, String> output = new HashMap<>();
        Map<String, String> inputMap = getInput(request);
        String remoteIpAddress = getRemoteIpAddress(request);
        for (IServerPlugin<Object, Object> plugin : allPlugins) {
            String tag = plugin.onSetupSyncTagToHandle();
            String tagInputJson = inputMap.get(tag);
            if (null == tagInputJson) {
                continue;
            }
            Object tmpOutput = plugin.onGetOutputClass().getConstructor().newInstance();
            plugin.onHandleSync(remoteIpAddress, GSON.fromJson(tagInputJson, plugin.onGetInputClass()), tmpOutput);
            output.put(tag, GSON.toJson(tmpOutput));
        }
        return output;
    }

    private Map<String, String> getInput(HttpServletRequest request) {
        Map<String, String> input = new HashMap<>();
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            input.put(name, request.getParameter(name));
        }
        return input;
    }

}
