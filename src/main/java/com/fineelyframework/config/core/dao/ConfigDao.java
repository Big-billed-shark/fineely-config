/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

/*
 * Powered By [spring-boot-framework]
 * Since 2015 - 2018
 */

package com.fineelyframework.config.core.dao;

import com.fineelyframework.config.core.entity.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

/**
 * @author Rey Kepler
 */
@ConditionalOnProperty(prefix = "fineely.config", name = "datasource", havingValue = "jpa")
public interface ConfigDao extends JpaRepositoryImplementation<Config, Integer>, ConfigDaoPlus {
}