package com.soybeany.rpc.registry;

import com.soybeany.rpc.core.model.ServerInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Soybeany
 * @date 2021/10/27
 */
@EqualsAndHashCode(exclude = "syncTime")
@AllArgsConstructor
@Data
public class ProviderResource {

    /**
     * 资源id
     */
    private String id;

    /**
     * 服务器信息
     */
    private ServerInfo info;

    /**
     * 同步时间
     */
    private long syncTime;

}
