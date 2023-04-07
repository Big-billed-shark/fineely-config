package com.fineelyframework.config;

import com.fineelyframework.config.core.entity.ConfigSupport;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Custom bean config registry.
 *
 * <p>Get basePackages and basePackageClasses through @{@link EnableAutoConfigScan}
 * <p>Scan all entities implements {@link ConfigSupport} injection {@link FineelyConfigServlet}
 * and {@link ConfigIntermediary}
 * @author Rey Kepler
 * @since 0.0.1
 * @see Reflections
 * @see ConfigSupport
 * @see FineelyConfigServlet
 * @see ImportBeanDefinitionRegistrar
 */
@Component
public class FineelyConfigAnnotationRegistry implements ImportBeanDefinitionRegistrar {

    /**
     * Register bean definitions as necessary based on the given annotation metadata of
     * the importing {@code @Configuration} class.
     * <p>Note that {@link BeanDefinitionRegistryPostProcessor} types may <em>not</em> be
     * registered here, due to lifecycle constraints related to {@code @Configuration}
     * class processing.
     * <p>The default implementation delegates to
     * {@link #registerBeanDefinitions(AnnotationMetadata, BeanDefinitionRegistry)}.
     * @param importingClassMetadata annotation metadata of the importing class
     * @param registry current bean definition registry
     * {@link ConfigurationClassPostProcessor#IMPORT_BEAN_NAME_GENERATOR} by default, or a
     * user-provided one if {@link ConfigurationClassPostProcessor#setBeanNameGenerator}
     * has been set. In the latter case, the passed-in strategy will be the same used for
     * component scanning in the containing application context (otherwise, the default
     * component-scan naming strategy is {@link AnnotationBeanNameGenerator#INSTANCE}).
     * @since 5.2
     * @see ConfigurationClassPostProcessor#IMPORT_BEAN_NAME_GENERATOR
     * @see ConfigurationClassPostProcessor#setBeanNameGenerator
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(EnableAutoConfigScan.class.getName());
        String[] basePackages = (String[]) annotationAttributes.get("basePackages");
        Reflections reflections = new Reflections(basePackages, new SubTypesScanner(), new TypeAnnotationsScanner());
        Set<Class<? extends ConfigSupport>> subTypes = reflections.getSubTypesOf(ConfigSupport.class);
        Class<? extends ConfigSupport>[] basePackageClasses = (Class<? extends ConfigSupport>[]) annotationAttributes.get("basePackageClasses");
        subTypes.addAll(Arrays.stream(basePackageClasses).collect(Collectors.toSet()));
        String requestMapping = (String) annotationAttributes.get("requestMapping");
        List<String> urlMappings = new ArrayList<>();
        Map<String, String> packageMap = new HashMap<>();
        for (Class<?> clazz : subTypes) {
            String packageName = clazz.getName();
            String[] packages = packageName.split("\\.");
            String className = packages[packages.length - 1];
            urlMappings.add(String.format(requestMapping + "get%s", className));
            urlMappings.add(String.format(requestMapping + "update%s", className));
            packageMap.put(className, packageName);
        }
        // 注入ServletRegistrationBean
        BeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClassName(ServletRegistrationBean.class.getName());
        MutablePropertyValues values = beanDefinition.getPropertyValues();
        values.addPropertyValue("servlet", new FineelyConfigServlet());
        values.addPropertyValue("urlMappings", urlMappings);
        registry.registerBeanDefinition("servletRegistrationBean", beanDefinition);
        // 注入配置中介
        BeanDefinition configIntermediary = new GenericBeanDefinition();
        configIntermediary.setBeanClassName(ConfigIntermediary.class.getName());
        MutablePropertyValues configIntermediaryValues = configIntermediary.getPropertyValues();
        configIntermediaryValues.addPropertyValue("packageMap", packageMap);
        configIntermediaryValues.addPropertyValue("requestMapping", requestMapping);
        registry.registerBeanDefinition("configIntermediary", configIntermediary);
    }
}