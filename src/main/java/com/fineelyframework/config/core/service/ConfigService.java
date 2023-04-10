package com.fineelyframework.config.core.service;


import com.fineelyframework.config.core.entity.ConfigSupport;

public interface ConfigService {

    <T extends ConfigSupport> void update(T configObj);

    <T extends ConfigSupport> T get(T configObj);

}
