package com.fineelyframework.config.core.service;


import com.fineelyframework.config.core.dao.ConfigDao;
import com.fineelyframework.config.core.entity.ConfigSupport;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Transactional
public class ConfigServiceImpl implements ConfigService {

    /**
     * 基本配置JPA操作
     */
    @Resource
    private ConfigDao configDao;


    /**
     * 更新Config
     **/
    public <T extends ConfigSupport> void update(T configObj) {
        configDao.update(configObj);
    }

    /**
     * 获得Config
     **/
    public <T extends ConfigSupport> T get(T configObj) {
        return configDao.get(configObj);
    }

}
