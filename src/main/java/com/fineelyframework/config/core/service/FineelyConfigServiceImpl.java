package com.fineelyframework.config.core.service;


import com.fineelyframework.config.core.dao.ConfigDao;
import com.fineelyframework.config.core.entity.ConfigSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;

@Transactional
public class FineelyConfigServiceImpl implements FineelyConfigService {

    @Autowired
    private ConfigDao configDao;


    public <T extends ConfigSupport> void update(T configObj) {
        configDao.update(configObj);
    }

    public <T extends ConfigSupport> T get(T configObj) {
        return configDao.get(configObj);
    }

    @Override
    public <T extends ConfigSupport> T get(Class<T> tClass) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return configDao.get(tClass);
    }

}
