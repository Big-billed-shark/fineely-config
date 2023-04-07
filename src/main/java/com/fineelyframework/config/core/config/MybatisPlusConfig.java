package com.fineelyframework.config.core.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "fineely.config", name = "datasource", havingValue = "mybatis")
@MapperScan(basePackages ={"com.fineelyframework.config.core.dao"})
public class MybatisPlusConfig {

}
