package com.fineelyframework.config.core.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ConditionalOnProperty(prefix = "fineely.config", name = "datasource", havingValue = "jpa")
@EnableJpaRepositories(basePackages = {"com.fineelyframework"})
@EntityScan({"com.fineelyframework"})
public class JpaConfig {

}
