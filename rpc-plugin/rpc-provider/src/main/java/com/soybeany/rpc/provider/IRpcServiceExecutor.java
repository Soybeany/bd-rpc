package com.soybeany.rpc.provider;

import com.soybeany.sync.core.model.SyncDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Soybeany
 * @date 2021/11/1
 */
public interface IRpcServiceExecutor {

    SyncDTO execute(HttpServletRequest request, HttpServletResponse response);

}
