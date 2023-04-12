package com.fineelyframework.config.core.service;


import com.fineelyframework.config.core.dao.ConfigDao;
import com.fineelyframework.config.core.entity.ConfigSupport;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Transactional
public class ConfigServiceImpl implements ConfigService {

    @Resource
    private ConfigDao configDao;


    public <T extends ConfigSupport> void update(T configObj) {
        configDao.update(configObj);
    }

    public <T extends ConfigSupport> T get(T configObj) {
        return configDao.get(configObj);
    }

}
