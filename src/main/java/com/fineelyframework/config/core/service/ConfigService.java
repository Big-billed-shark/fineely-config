package com.fineelyframework.config.core.service;


import com.fineelyframework.config.core.entity.ConfigSupport;

import java.lang.reflect.InvocationTargetException;

public interface ConfigService {

    <T extends ConfigSupport> void update(T configObj);

    <T extends ConfigSupport> T get(T configObj);

    <T extends ConfigSupport> T get(Class<T> tClass) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;

}
