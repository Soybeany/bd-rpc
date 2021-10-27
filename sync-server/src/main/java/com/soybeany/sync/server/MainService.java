package com.soybeany.sync.server;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Soybeany
 * @date 2021/10/26
 */
public interface MainService {

    Map<String, String> sync(HttpServletRequest request);

}
