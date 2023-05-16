package com.fineelyframework.config.core.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fineelyframework.config.core.entity.ConfigPlus;
import com.fineelyframework.config.core.entity.ConfigSupport;
import com.fineelyframework.config.core.utils.TypeJudgmentUtil;
import org.apache.ibatis.annotations.Mapper;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Mapper
public interface ConfigMapper extends BaseMapper<ConfigPlus>, ConfigDaoPlus {

    default <T extends ConfigSupport> void update(T configSupport) {
        Field[] fields = configSupport.getClass().getDeclaredFields();
        String configCategory = configSupport.getClass().getSimpleName();
        List<ConfigPlus> configs = getConfigByCategoryAndCode(configCategory, TypeJudgmentUtil.getConfigCodes(fields));
        Field.setAccessible(fields, true);
        for (Field field : fields) {
            String configCode = field.getName();
            Optional<ConfigPlus> optional = configs.stream().filter(p -> p.getConfigCode().equals(configCode)).findFirst();
            try {
                Object configValue = field.get(configSupport);
                if (optional.isPresent()) {
                    ConfigPlus config = optional.get();
                    config.setLastModifyTime(LocalDateTime.now());
                    config.setConfigValue(Objects.isNull(configValue) ? null : configValue.toString());
                    this.updateById(config);
                } else {
                    ConfigPlus config = new ConfigPlus(configCategory, configCode, configValue);
                    this.insert(config);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    default <T extends ConfigSupport> T get(T configSupport) {
        Field[] fields = configSupport.getClass().getDeclaredFields();
        String configCategory = configSupport.getClass().getSimpleName();
        Field.setAccessible(fields, true);
        List<ConfigPlus> configs = getConfigByCategoryAndCode(configCategory, TypeJudgmentUtil.getConfigCodes(fields));

        for (Field field : fields) {
            String configCode = field.getName();
            Optional<ConfigPlus> optional = configs.stream().filter(p -> p.getConfigCode().equals(configCode)).findFirst();
            if (optional.isPresent()) {
                TypeJudgmentUtil.set(configSupport, field, optional.get().getConfigValue());
            } else {
                Object configValue = TypeJudgmentUtil.get(configSupport, field);
                ConfigPlus config = new ConfigPlus(configCategory, configCode, configValue);
                this.insert(config);
            }
        }

        return configSupport;
    }

    private List<ConfigPlus> getConfigByCategoryAndCode(String configCategory, Object[] configCodes) {
        return this.selectList(Wrappers.<ConfigPlus>query().lambda().eq(ConfigPlus::getConfigCategory, configCategory)
                .in(ConfigPlus::getConfigCode, configCodes));
    }
}
