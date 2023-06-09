package com.fineelyframework.config;

import com.alibaba.fastjson2.JSONObject;
import com.fineelyframework.config.core.entity.ConfigSupport;
import com.fineelyframework.config.core.service.FineelyConfigService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * This is the config intermediary class.
 *
 * @author Rey Kepler
 * @since 0.0.1
 */
public class ConfigIntermediary {

    /**
     * Configure service get configuration.
     */
    @Autowired
    private FineelyConfigService fineelyConfigService;

    /**
     * Configure class package name, key is class name, value is class package name.
     */
    private Map<String, String> packageMap;

    /**
     * Request mapping prefix. That's it @RequestMapping value.
     */
    private String requestMapping;

    /**
     * Mapping out package address by class name<p>
     * Creating instances from reflect<p>
     * @param className Labeled class name
     * @see com.fineelyframework.config.core.entity.ConfigSupport
     * @return Labeled class
     * @since 0.0.1
     */
    public ConfigSupport getConfigByObject(String className) {
        try {
            ConfigSupport myInstance = (ConfigSupport) Class.forName(packageMap.get(className)).getDeclaredConstructor().newInstance();
            return fineelyConfigService.get(myInstance);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Mapping out package address by class name<p>
     * @param className Labeled class name
     * @param configObjString Configure the json string of the class
     * @see com.fineelyframework.config.core.entity.ConfigSupport
     * @since 0.0.1
     */
    public void updateConfigByObject(String className, String configObjString) {
        try {
            Class<?> aClass = Class.forName(packageMap.get(className));
            fineelyConfigService.update((ConfigSupport) JSONObject.parseObject(configObjString, aClass));
        } catch (Exception ignored) {
        }
    }

    public FineelyConfigService getFineelyConfigService() {
        return fineelyConfigService;
    }

    public void setFineelyConfigService(FineelyConfigService fineelyConfigService) {
        this.fineelyConfigService = fineelyConfigService;
    }

    public String getRequestMapping() {
        return requestMapping;
    }

    public void setRequestMapping(String requestMapping) {
        this.requestMapping = requestMapping;
    }

    public Map<String, String> getPackageMap() {
        return packageMap;
    }

    public void setPackageMap(Map<String, String> packageMap) {
        this.packageMap = packageMap;
    }
}
