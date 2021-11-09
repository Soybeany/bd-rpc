package com.soybeany.rpc.registry;

import java.util.Set;

/**
 * @author Soybeany
 * @date 2021/11/1
 */
public interface IResourceManager {

    Set<ProviderResource> load(String id);

    void save(ProviderResource resource);

}
