package com.fineelyframework.config.core.dao;

import com.fineelyframework.config.core.entity.ConfigSupport;

import java.lang.reflect.InvocationTargetException;

public interface ConfigDaoPlus {

    <T extends ConfigSupport> void update(T configSupport);

    <T extends ConfigSupport> T get(T configSupport);

    <T extends ConfigSupport> T get(Class<T> tClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException;
}
