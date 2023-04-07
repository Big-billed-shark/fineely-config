package com.fineelyframework.config.core.config;

import com.fineelyframework.config.core.service.ConfigService;
import com.fineelyframework.config.core.service.ConfigServiceImpl;
import com.fineelyframework.config.core.service.ConfigServicePlusImpl;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * fineely配置类
 */
@Configuration
@ConfigurationProperties(prefix = "fineely.config")
@AutoConfigureAfter(MybatisPlusConfig.class)
public class DatasourceConfig {

    private String datasource = "";

    @Bean
    @ConditionalOnMissingBean
    public ConfigService configService() {
        switch (datasource) {
            case "mybatis":
                return new ConfigServicePlusImpl();
            case "jpa":
                return new ConfigServiceImpl();
            default:
                throw new BeanCreationException(ConfigService.class.getSimpleName(),
                        "missing configuration \"fineely.config.datasource\", Please fill in \"mybatis\" or \"jpa\"");
        }
    }

    public String getDatasource() {
        return datasource;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }

}
