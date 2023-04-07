package com.fineelyframework.config.core.dao;

import com.fineelyframework.config.core.entity.ConfigSupport;

public interface ConfigDaoPlus {

    <T extends ConfigSupport> void update(T configSupport);

    <T extends ConfigSupport> T get(T configSupport);
}
