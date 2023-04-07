package com.fineelyframework.config.core.service;


import com.fineelyframework.config.core.entity.ConfigSupport;

public interface ConfigService {

    /**
     * 更新Config
     **/
    <T extends ConfigSupport> void update(T configObj);

    /**
     * 获得Config
     **/
    <T extends ConfigSupport> T get(T configObj);

}
