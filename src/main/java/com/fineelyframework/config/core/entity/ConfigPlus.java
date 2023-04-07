package com.fineelyframework.config.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import javax.persistence.*;

/**
 * @author Rey Kepler
 */
@TableName("config")
public class ConfigPlus extends Config {

    /**
     * 配置主键
     */
    @TableId(type = IdType.AUTO)
    private Integer configId;

    public ConfigPlus() {
    }

    public ConfigPlus(String configCategory, String configCode, Object configValue) {
        super(configCategory, configCode, configValue);
    }
}

