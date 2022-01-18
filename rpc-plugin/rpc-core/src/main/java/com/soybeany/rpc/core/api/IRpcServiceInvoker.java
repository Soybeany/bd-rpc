package com.soybeany.rpc.core.api;

import com.soybeany.sync.core.model.SyncDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Soybeany
 * @date 2021/11/1
 */
public interface IRpcServiceInvoker {

    SyncDTO invoke(HttpServletRequest request, HttpServletResponse response);

}
