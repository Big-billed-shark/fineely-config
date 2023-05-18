package com.fineelyframework.config.core.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * @author Rey Kepler
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "config")
public class Config implements Serializable {

    private static final long serialVersionUID = 3434155825314659842L;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(unique = true, nullable = false, length = 10)
    private int configId;

    @Column(nullable = false, length = 64)
    private String configCategory;

    @Column(nullable = false, length = 64)
    private String configCode;

    @Column
    private String configValue;

    @Column(nullable = false)
    private LocalDateTime lastModifyTime;

    public Config() {
    }

    public Config(String configCategory, String configCode, Object configValue) {
        this.configCode = configCode;
        if (configValue != null) {
            this.configValue = configValue.toString();
        }
        this.configCategory = configCategory;
        this.lastModifyTime = LocalDateTime.now();
    }

    public Integer getConfigId() {
        return configId;
    }

    public void setConfigId(int configId) {
        this.configId = configId;
    }

    public String getConfigCode() {
        return configCode;
    }

    public void setConfigCode(String configCode) {
        this.configCode = configCode;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getConfigCategory() {
        return configCategory;
    }

    public void setConfigCategory(String configCategory) {
        this.configCategory = configCategory;
    }

    public LocalDateTime getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(LocalDateTime lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }
}

