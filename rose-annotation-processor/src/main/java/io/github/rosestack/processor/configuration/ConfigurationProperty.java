package io.github.rosestack.processor.configuration;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * The metadata annotation used to declare on the Java field whose modifiers usually are static and final
 * for the configuration property.
 * <p>
 * The module that named "microsphere-annotation-processor" generates the metadata resource in the classpath.
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy<a/>
 * @since 1.0.0
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
public @interface ConfigurationProperty {

    /**
     * JDK System Properties
     */
    String SYSTEM_PROPERTIES_SOURCE = "system-properties";

    /**
     * OS Environment Variables
     */
    String ENVIRONMENT_VARIABLES_SOURCE = "environment-variables";

    /**
     * Application
     */
    String APPLICATION_SOURCE = "application";

    /**
     * The name of the configuration property.
     *
     * @return empty content as default
     */
    String name() default "";

    /**
     * The type of the configuration property.
     *
     * @reurn the default value is {@link String}
     */
    Class<?> type() default String.class;

    /**
     * The default value of the configuration property.
     *
     * @return empty content as default
     */
    String defaultValue() default "";

    /**
     * Whether the configuration property is required.
     *
     * @return true if required, otherwise false
     */
    boolean required() default false;

    /**
     * The description of the configuration property.
     *
     * @return empty content as default
     */
    String description() default "";

    /**
     * The source of the configuration property.
     *
     * @return empty array as default
     */
    String[] source() default {};
}
