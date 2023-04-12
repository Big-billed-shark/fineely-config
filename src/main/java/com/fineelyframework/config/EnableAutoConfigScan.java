package com.fineelyframework.config;

import com.fineelyframework.config.core.config.DatasourceConfig;
import com.fineelyframework.config.core.entity.ConfigSupport;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * Enable automatic configuration class scanning
 * <p>@EnableConfigScan(basePackage = "com.fineely.entity", requestMapping = "/rest/config/")
 * <p>@EnableConfigScan(basePackage = "com.fineely.entity")
 *
 * @author Rey Kepler
 * @since 0.0.1
 * @see DatasourceConfig
 * @see FineelyConfigAnnotationRegistry
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({DatasourceConfig.class, FineelyConfigAnnotationRegistry.class})
public @interface EnableAutoConfigScan {

    /**
     * Alias for the {@link #basePackages()} attribute. Allows for more concise annotation
     * declarations e.g.: {@code @EnableAutoConfigScan("org.my.pkg")} instead of
     * {@code @EnableAutoConfigScan(basePackages="org.my.pkg")}.
     * @return the base packages to scan
     */
    @AliasFor("basePackages")
    String[] value() default {};

    /**
     * Base packages to scan for entities. {@link #value()} is an alias for (and mutually
     * exclusive with) this attribute.
     * <p>
     * Use {@link #basePackageClasses()} for a type-safe alternative to String-based
     * package names.
     * @see com.fineelyframework.config.core.entity.ConfigSupport
     * @return the base packages to scan
     */
    @AliasFor("value")
    String[] basePackages() default {};


    /**
     * Type-safe alternative to {@link #basePackages()} for specifying the packages to
     * scan for entities. The package of each class specified will be scanned.
     * <p>
     * Consider creating a special no-op marker class or interface in each package that
     * serves no purpose other than being referenced by this attribute.
     * @return classes from the base packages to scan
     */
    Class<? extends ConfigSupport>[] basePackageClasses() default {};

    /**
     * Request mapping prefix. That's it @RequestMapping value, default /rest/config/.
     */
    String requestMapping() default "/rest/config/";
}
