package com.soybeany.sync.server;

import com.soybeany.sync.core.api.IBasePlugin;
import com.soybeany.sync.core.api.IServerPlugin;
import com.soybeany.sync.core.exception.SyncException;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.soybeany.sync.core.util.RequestUtils.GSON;

/**
 * @author Soybeany
 * @date 2021/10/26
 */
public class SyncServerService {

    private final List<IServerPlugin<Object, Object>> allPlugins;

    public SyncServerService(IServerPlugin<Object, Object>[] plugins) {
        allPlugins = Arrays.asList(plugins);
        IBasePlugin.checkPlugins(allPlugins);
        Collections.sort(allPlugins);
    }

    public Map<String, String> sync(HttpServletRequest request) throws SyncException {
        Map<String, String> output = new HashMap<>();
        Map<String, String> inputMap = getInput(request);
        for (IServerPlugin<Object, Object> plugin : allPlugins) {
            String tag = plugin.onSetupSyncTagToHandle();
            String tagInputJson = inputMap.get(tag);
            if (null == tagInputJson) {
                continue;
            }
            try {
                Object tmpOutput = plugin.onGetOutputClass().getConstructor().newInstance();
                plugin.onHandleSync(GSON.fromJson(tagInputJson, plugin.onGetInputClass()), tmpOutput);
                output.put(tag, GSON.toJson(tmpOutput));
            } catch (SyncException e) {
                throw e;
            } catch (Exception e) {
                throw new SyncException(e.getMessage());
            }
        }
        return output;
    }

    // ***********************内部方法****************************

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
