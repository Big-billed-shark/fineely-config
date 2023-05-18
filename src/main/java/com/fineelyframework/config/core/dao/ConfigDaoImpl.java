package com.fineelyframework.config.core.dao;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fineelyframework.config.core.entity.Config;
import com.fineelyframework.config.core.entity.ConfigSupport;
import com.fineelyframework.config.core.utils.TypeJudgmentUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.*;

public class ConfigDaoImpl implements ConfigDaoPlus {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public <T extends ConfigSupport> void update(T configSupport) {
        Field[] fields = configSupport.getClass().getDeclaredFields();
        String configCategory = configSupport.getClass().getSimpleName();
        List<Config> configs = getConfigByCategoryAndCode(configCategory, TypeJudgmentUtil.getConfigCodes(fields));
        Field.setAccessible(fields, true);
        for (Field field : fields) {
            String configCode = field.getName();
            Optional<Config> optional = configs.stream().filter(p -> p.getConfigCode().equals(configCode)).findFirst();
            try {
                Object configValue = field.get(configSupport);
                if (optional.isPresent()) {
                    Config config = optional.get();
                    config.setLastModifyTime(LocalDateTime.now());
                    String value;
                    if (Objects.isNull(configValue)) {
                        value = null;
                    } else {
                        if (configValue instanceof List) {
                            value = JSONArray.toJSONString(configValue);
                        } else if (configValue instanceof Map) {
                            value = JSONObject.toJSONString(configValue);
                        } else if (configValue instanceof Set) {
                            value = JSONObject.toJSONString(configValue);
                        } else if (TypeJudgmentUtil.isBasicType(configValue.getClass())) {
                            value = configValue.toString();
                        } else {
                            value = JSONObject.toJSONString(configValue);
                        }
                    }
                    config.setConfigValue(value);
                } else {
                    Config config = new Config(configCategory, configCode, configValue);
                    entityManager.persist(config);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据ID得到Config
     **/
    @Override
    public <T extends ConfigSupport> T get(T configSupport) {
        Field[] fields = configSupport.getClass().getDeclaredFields();
        String configCategory = configSupport.getClass().getSimpleName();
        Field.setAccessible(fields, true);
        List<Config> configs = getConfigByCategoryAndCode(configCategory, TypeJudgmentUtil.getConfigCodes(fields));

        for (Field field : fields) {
            String configCode = field.getName();
            Optional<Config> optional = configs.stream().filter(p -> p.getConfigCode().equals(configCode)).findFirst();
            if (optional.isPresent()) {
                TypeJudgmentUtil.set(configSupport, field, optional.get().getConfigValue());
            } else {
                Object configValue = TypeJudgmentUtil.get(configSupport, field);
                Config config = new Config(configCategory, configCode, configValue);
                entityManager.persist(config);
            }
        }

        return configSupport;
    }

    @Override
    public <T extends ConfigSupport> T get(Class<T> tClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        T t = tClass.getDeclaredConstructor().newInstance();
        return this.get(t);
    }

    private List<Config> getConfigByCategoryAndCode(String configCategory, Object[] configCodes) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Config> query = builder.createQuery(Config.class);
        Root<Config> root = query.from(Config.class);
        query.select(root);
        query.where(builder.and(builder.equal(root.get("configCategory"), configCategory), root.get("configCode").in(configCodes)));
        return entityManager.createQuery(query).getResultList();
    }
}
