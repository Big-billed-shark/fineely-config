package com.fineelyframework.config.core.config;

import com.fineelyframework.config.core.service.FineelyConfigServiceImpl;
import com.fineelyframework.config.core.service.FineelyConfigService;
import com.fineelyframework.config.core.service.FineelyConfigServicePlusImpl;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * fineely Datasource configuration
 */
@Configuration
@ConfigurationProperties(prefix = "fineely.config")
@AutoConfigureAfter({MybatisPlusConfig.class, JpaConfig.class})
@ComponentScan({"com.fineelyframework"})
public class DatasourceConfig {

    private String datasource = "";

    @Bean
    @ConditionalOnMissingBean
    public FineelyConfigService fineelyConfigService() {
        switch (datasource) {
            case "mybatis":
                return new FineelyConfigServicePlusImpl();
            case "jpa":
                return new FineelyConfigServiceImpl();
            default:
                throw new BeanCreationException(FineelyConfigService.class.getSimpleName(),
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
