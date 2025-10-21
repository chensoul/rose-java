package io.github.rosestack.processor.configuration;

/**
 * Test constants for configuration property annotation processor.
 *
 * @author chensoul
 * @since 1.0.0
 */
public final class ConfigurationPropertyTestConstants {

    @ConfigurationProperty(
            name = "test.string.property",
            type = String.class,
            defaultValue = "default-value",
            required = false,
            description = "A test string property",
            source = {ConfigurationProperty.SYSTEM_PROPERTIES_SOURCE, ConfigurationProperty.APPLICATION_SOURCE})
    public static final String STRING_PROPERTY = "test.string.property";

    @ConfigurationProperty(
            name = "test.int.property",
            type = int.class,
            defaultValue = "100",
            required = true,
            description = "A test integer property",
            source = {ConfigurationProperty.SYSTEM_PROPERTIES_SOURCE})
    public static final String INT_PROPERTY = "test.int.property";

    @ConfigurationProperty(
            name = "test.boolean.property",
            type = boolean.class,
            defaultValue = "true",
            required = false,
            description = "A test boolean property",
            source = {ConfigurationProperty.ENVIRONMENT_VARIABLES_SOURCE})
    public static final String BOOLEAN_PROPERTY = "test.boolean.property";

    @ConfigurationProperty(
            name = "test.array.property",
            type = String[].class,
            defaultValue = "value1,value2,value3",
            required = false,
            description = "A test array property with special characters: /path/to/file",
            source = {ConfigurationProperty.APPLICATION_SOURCE})
    public static final String ARRAY_PROPERTY = "test.array.property";

    @ConfigurationProperty(
            name = "test.long.property",
            type = long.class,
            defaultValue = "9223372036854775807",
            required = false,
            description = "A test long property\nwith multiple lines\nand special characters: \"quoted\"",
            source = {ConfigurationProperty.SYSTEM_PROPERTIES_SOURCE})
    public static final String LONG_PROPERTY = "test.long.property";

    private ConfigurationPropertyTestConstants() {
        throw new UnsupportedOperationException("Utility class");
    }
}
