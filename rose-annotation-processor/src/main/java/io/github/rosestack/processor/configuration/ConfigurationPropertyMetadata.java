package io.github.rosestack.processor.configuration;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Metadata for a configuration property annotated with {@link ConfigurationProperty}.
 *
 * @author chensoul
 * @since 1.0.0
 */
public class ConfigurationPropertyMetadata {

    private final String name;
    private final String type;
    private final String defaultValue;
    private final boolean required;
    private final String description;
    private final Metadata metadata;

    public ConfigurationPropertyMetadata(
            String name,
            String type,
            String defaultValue,
            boolean required,
            String description,
            String[] sources,
            String declaredClass,
            String declaredField) {
        this.name = name;
        this.type = type;
        this.defaultValue = defaultValue;
        this.required = required;
        this.description = description;
        this.metadata = new Metadata(sources, declaredClass, declaredField);
    }

    /**
     * Convert to JSON map representation.
     *
     * @return JSON map
     */
    public Map<String, Object> toJsonMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("name", name);
        map.put("type", type);

        if (defaultValue != null && !defaultValue.isEmpty()) {
            map.put("defaultValue", defaultValue);
        }

        map.put("required", required);

        if (description != null && !description.isEmpty()) {
            map.put("description", description);
        }

        map.put("metadata", metadata.toJsonMap());

        return map;
    }

    /**
     * Convert to JSON string.
     *
     * @param indent the indent level
     * @return JSON string
     */
    public String toJson(int indent) {
        return JsonBuilder.buildObject(indent, toJsonMap());
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public boolean isRequired() {
        return required;
    }

    public String getDescription() {
        return description;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    /**
     * Metadata inner class containing sources, declared class, and declared field.
     */
    public static class Metadata {
        private final List<String> sources;
        private final String declaredClass;
        private final String declaredField;

        public Metadata(String[] sources, String declaredClass, String declaredField) {
            this.sources = Arrays.stream(sources).collect(Collectors.toList());
            this.declaredClass = declaredClass;
            this.declaredField = declaredField;
        }

        /**
         * Convert to JSON map representation.
         *
         * @return JSON map
         */
        public Map<String, Object> toJsonMap() {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("sources", sources);
            map.put("declaredClass", declaredClass);
            map.put("declaredField", declaredField);
            return map;
        }

        public List<String> getSources() {
            return sources;
        }

        public String getDeclaredClass() {
            return declaredClass;
        }

        public String getDeclaredField() {
            return declaredField;
        }
    }
}
