package com.soybeany.sync.core.api;

import com.soybeany.sync.core.model.SyncDTO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Soybeany
 * @date 2021/12/16
 */
public interface IServerSyncer {

    SyncDTO sync(HttpServletRequest request);

}
