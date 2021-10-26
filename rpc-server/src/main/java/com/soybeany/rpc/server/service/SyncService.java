package com.soybeany.rpc.server.service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Soybeany
 * @date 2021/10/26
 */
public interface SyncService {

    Map<String, String> sync(HttpServletRequest request);

}
