package com.fineelyframework.config.core.service;

import com.fineelyframework.config.core.dao.ConfigMapper;
import com.fineelyframework.config.core.entity.ConfigSupport;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationTargetException;


public class FineelyConfigServicePlusImpl implements FineelyConfigService {

    @Autowired
    private ConfigMapper configMapper;

    public <T extends ConfigSupport> void update(T configSupport) {
        configMapper.update(configSupport);
    }

    public <T extends ConfigSupport> T get(T configSupport) {
        return configMapper.get(configSupport);
    }

    @Override
    public <T extends ConfigSupport> T get(Class<T> tClass) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return configMapper.get(tClass);
    }

}
