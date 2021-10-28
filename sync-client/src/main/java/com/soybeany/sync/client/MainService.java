package com.soybeany.sync.client;

import com.soybeany.sync.core.api.IClientPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

/**
 * @author Soybeany
 * @date 2021/10/27
 */
@Service
public class MainService {

    @Autowired
    private List<IClientPlugin> allPlugins;

    @PostConstruct
    private void onStart() {
        Collections.sort(allPlugins);

    }

}
