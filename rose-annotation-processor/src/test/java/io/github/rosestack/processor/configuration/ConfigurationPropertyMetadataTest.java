package io.github.rosestack.processor.configuration;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link ConfigurationPropertyMetadata}.
 *
 * @author chensoul
 * @since 1.0.0
 */
class ConfigurationPropertyMetadataTest {

    @Test
    void testCreateMetadata() {
        ConfigurationPropertyMetadata metadata = new ConfigurationPropertyMetadata(
                "test.property",
                "java.lang.String",
                "default-value",
                true,
                "Test property",
                new String[] {"system-properties"},
                "com.example.Test",
                "TEST_PROPERTY");

        assertEquals("test.property", metadata.getName());
        assertEquals("java.lang.String", metadata.getType());
        assertEquals("default-value", metadata.getDefaultValue());
        assertTrue(metadata.isRequired());
        assertEquals("Test property", metadata.getDescription());
        assertNotNull(metadata.getMetadata());
    }

    @Test
    void testToJsonMap() {
        ConfigurationPropertyMetadata metadata = new ConfigurationPropertyMetadata(
                "test.property",
                "int",
                "100",
                false,
                "Test integer property",
                new String[] {"application", "system-properties"},
                "com.example.Config",
                "INT_PROPERTY");

        Map<String, Object> jsonMap = metadata.toJsonMap();

        assertEquals("test.property", jsonMap.get("name"));
        assertEquals("int", jsonMap.get("type"));
        assertEquals("100", jsonMap.get("defaultValue"));
        assertEquals(false, jsonMap.get("required"));
        assertEquals("Test integer property", jsonMap.get("description"));
        assertTrue(jsonMap.containsKey("metadata"));
    }

    @Test
    void testToJsonMapWithEmptyDefaultValue() {
        ConfigurationPropertyMetadata metadata = new ConfigurationPropertyMetadata(
                "test.property",
                "boolean",
                "",
                false,
                "Test boolean property",
                new String[] {"system-properties"},
                "com.example.Config",
                "BOOLEAN_PROPERTY");

        Map<String, Object> jsonMap = metadata.toJsonMap();

        assertFalse(jsonMap.containsKey("defaultValue"));
    }

    @Test
    void testToJsonMapWithEmptyDescription() {
        ConfigurationPropertyMetadata metadata = new ConfigurationPropertyMetadata(
                "test.property",
                "java.lang.String",
                "value",
                false,
                "",
                new String[] {"system-properties"},
                "com.example.Config",
                "STRING_PROPERTY");

        Map<String, Object> jsonMap = metadata.toJsonMap();

        assertFalse(jsonMap.containsKey("description"));
    }

    @Test
    void testMetadataToJsonMap() {
        ConfigurationPropertyMetadata.Metadata metadata = new ConfigurationPropertyMetadata.Metadata(
                new String[] {"system-properties", "application"}, "com.example.Test", "TEST_FIELD");

        Map<String, Object> jsonMap = metadata.toJsonMap();

        assertTrue(jsonMap.containsKey("sources"));
        assertEquals("com.example.Test", jsonMap.get("declaredClass"));
        assertEquals("TEST_FIELD", jsonMap.get("declaredField"));
    }

    @Test
    void testToJson() {
        ConfigurationPropertyMetadata metadata = new ConfigurationPropertyMetadata(
                "test.property",
                "java.lang.String",
                "default",
                true,
                "Test property",
                new String[] {"system-properties"},
                "com.example.Test",
                "TEST_PROPERTY");

        String json = metadata.toJson(0);

        assertTrue(json.contains("\"name\": \"test.property\""));
        assertTrue(json.contains("\"type\": \"java.lang.String\""));
        assertTrue(json.contains("\"defaultValue\": \"default\""));
        assertTrue(json.contains("\"required\": true"));
        assertTrue(json.contains("\"description\": \"Test property\""));
        assertTrue(json.contains("\"metadata\":"));
    }

    @Test
    void testMetadataGetters() {
        ConfigurationPropertyMetadata.Metadata metadata = new ConfigurationPropertyMetadata.Metadata(
                new String[] {"source1", "source2"}, "com.example.Class", "FIELD_NAME");

        assertEquals(2, metadata.getSources().size());
        assertEquals("source1", metadata.getSources().get(0));
        assertEquals("source2", metadata.getSources().get(1));
        assertEquals("com.example.Class", metadata.getDeclaredClass());
        assertEquals("FIELD_NAME", metadata.getDeclaredField());
    }

    @Test
    void testMetadataWithEmptySources() {
        ConfigurationPropertyMetadata.Metadata metadata =
                new ConfigurationPropertyMetadata.Metadata(new String[] {}, "TestClass", "testField");

        assertNotNull(metadata.getSources());
        assertTrue(metadata.getSources().isEmpty());
        assertEquals("TestClass", metadata.getDeclaredClass());
        assertEquals("testField", metadata.getDeclaredField());
    }

    @Test
    void testToJsonWithZeroIndent() {
        ConfigurationPropertyMetadata metadata =
                new ConfigurationPropertyMetadata("test", "String", "", false, "", new String[] {}, "", "");

        String json = metadata.toJson(0);

        // With indent 0, there will still be newlines but no double spaces
        assertTrue(json.contains("\"name\": \"test\""));
    }

    @Test
    void testToJsonMapWithAllFields() {
        ConfigurationPropertyMetadata metadata = new ConfigurationPropertyMetadata(
                "app.config",
                "java.lang.Integer",
                "100",
                false,
                "Application config",
                new String[] {"AppConfig"},
                "AppConfig",
                "config");

        Map<String, Object> map = metadata.toJsonMap();

        assertEquals(6, map.size()); // name, type, defaultValue, required, description, metadata
        assertEquals("app.config", map.get("name"));
        assertEquals("java.lang.Integer", map.get("type"));
        assertEquals("100", map.get("defaultValue"));
        assertEquals(false, map.get("required"));
        assertEquals("Application config", map.get("description"));
        assertNotNull(map.get("metadata"));
    }

    @Test
    void testMetadataToString() {
        ConfigurationPropertyMetadata.Metadata metadata =
                new ConfigurationPropertyMetadata.Metadata(new String[] {"source1"}, "TestClass", "field1");

        String toString = metadata.toString();

        assertNotNull(toString);
        // Just verify toString returns a non-empty string
        assertFalse(toString.isEmpty());
    }

    @Test
    void testConfigurationPropertyMetadataToString() {
        ConfigurationPropertyMetadata metadata =
                new ConfigurationPropertyMetadata("test.property", "String", "", false, "", new String[] {}, "", "");

        String toString = metadata.toString();

        assertNotNull(toString);
        // Just verify toString returns a non-empty string
        assertFalse(toString.isEmpty());
    }
}
