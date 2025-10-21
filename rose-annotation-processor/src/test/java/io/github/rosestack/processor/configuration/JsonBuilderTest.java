package io.github.rosestack.processor.configuration;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link JsonBuilder}.
 *
 * @author chensoul
 * @since 1.0.0
 */
class JsonBuilderTest {

    @Test
    void testEscapeNull() {
        assertNull(JsonBuilder.escape(null));
    }

    @Test
    void testEscapeEmpty() {
        assertEquals("", JsonBuilder.escape(""));
    }

    @Test
    void testEscapeNormalString() {
        assertEquals("hello world", JsonBuilder.escape("hello world"));
    }

    @Test
    void testEscapeSpecialCharacters() {
        // Test quote
        assertEquals("hello \\\"world\\\"", JsonBuilder.escape("hello \"world\""));

        // Test backslash
        assertEquals("path\\\\to\\\\file", JsonBuilder.escape("path\\to\\file"));

        // Test forward slash
        assertEquals("path\\/to\\/file", JsonBuilder.escape("path/to/file"));

        // Test newline
        assertEquals("line1\\nline2", JsonBuilder.escape("line1\nline2"));

        // Test tab
        assertEquals("col1\\tcol2", JsonBuilder.escape("col1\tcol2"));

        // Test carriage return
        assertEquals("line1\\rline2", JsonBuilder.escape("line1\rline2"));
    }

    @Test
    void testEscapeControlCharacters() {
        // Test control character (ASCII 1)
        String result = JsonBuilder.escape("\u0001");
        assertTrue(result.contains("\\u0001"));
    }

    @Test
    void testBuildArrayEmpty() {
        assertEquals("[]", JsonBuilder.buildArray(0, null));
        assertEquals("[]", JsonBuilder.buildArray(0, Arrays.asList()));
    }

    @Test
    void testBuildArrayWithStrings() {
        List<String> items = Arrays.asList("item1", "item2", "item3");
        String json = JsonBuilder.buildArray(0, items);

        assertTrue(json.contains("\"item1\""));
        assertTrue(json.contains("\"item2\""));
        assertTrue(json.contains("\"item3\""));
        assertTrue(json.startsWith("["));
        assertTrue(json.endsWith("]"));
    }

    @Test
    void testBuildArrayWithNumbers() {
        List<Integer> items = Arrays.asList(1, 2, 3);
        String json = JsonBuilder.buildArray(0, items);

        assertTrue(json.contains("1"));
        assertTrue(json.contains("2"));
        assertTrue(json.contains("3"));
    }

    @Test
    void testBuildArrayWithBooleans() {
        List<Boolean> items = Arrays.asList(true, false);
        String json = JsonBuilder.buildArray(0, items);

        assertTrue(json.contains("true"));
        assertTrue(json.contains("false"));
    }

    @Test
    void testBuildArrayWithNull() {
        List<Object> items = Arrays.asList("value", null);
        String json = JsonBuilder.buildArray(0, items);

        assertTrue(json.contains("\"value\""));
        assertTrue(json.contains("null"));
    }

    @Test
    void testBuildObjectEmpty() {
        assertEquals("{}", JsonBuilder.buildObject(0, null));
        assertEquals("{}", JsonBuilder.buildObject(0, new LinkedHashMap<>()));
    }

    @Test
    void testBuildObjectWithStrings() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("name", "John");
        map.put("email", "john@example.com");

        String json = JsonBuilder.buildObject(0, map);

        assertTrue(json.contains("\"name\": \"John\""));
        assertTrue(json.contains("\"email\": \"john@example.com\""));
        assertTrue(json.startsWith("{"));
        assertTrue(json.endsWith("}"));
    }

    @Test
    void testBuildObjectWithNumbers() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("age", 30);
        map.put("score", 95.5);

        String json = JsonBuilder.buildObject(0, map);

        assertTrue(json.contains("\"age\": 30"));
        assertTrue(json.contains("\"score\": 95.5"));
    }

    @Test
    void testBuildObjectWithBoolean() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("active", true);
        map.put("deleted", false);

        String json = JsonBuilder.buildObject(0, map);

        assertTrue(json.contains("\"active\": true"));
        assertTrue(json.contains("\"deleted\": false"));
    }

    @Test
    void testBuildObjectWithNull() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("value", null);

        String json = JsonBuilder.buildObject(0, map);

        assertTrue(json.contains("\"value\": null"));
    }

    @Test
    void testBuildObjectWithNestedArray() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("items", Arrays.asList("a", "b", "c"));

        String json = JsonBuilder.buildObject(0, map);

        assertTrue(json.contains("\"items\":"));
        assertTrue(json.contains("\"a\""));
        assertTrue(json.contains("\"b\""));
        assertTrue(json.contains("\"c\""));
    }

    @Test
    void testBuildObjectWithNestedObject() {
        Map<String, Object> nested = new LinkedHashMap<>();
        nested.put("city", "Beijing");
        nested.put("country", "China");

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("address", nested);

        String json = JsonBuilder.buildObject(0, map);

        assertTrue(json.contains("\"address\":"));
        assertTrue(json.contains("\"city\": \"Beijing\""));
        assertTrue(json.contains("\"country\": \"China\""));
    }

    @Test
    void testBuildComplexStructure() {
        Map<String, Object> metadata = new LinkedHashMap<>();
        metadata.put("sources", Arrays.asList("system-properties", "application"));
        metadata.put("declaredClass", "com.example.Test");

        Map<String, Object> property = new LinkedHashMap<>();
        property.put("name", "test.property");
        property.put("type", "java.lang.String");
        property.put("required", false);
        property.put("metadata", metadata);

        String json = JsonBuilder.buildObject(0, property);

        assertTrue(json.contains("\"name\": \"test.property\""));
        assertTrue(json.contains("\"type\": \"java.lang.String\""));
        assertTrue(json.contains("\"required\": false"));
        assertTrue(json.contains("\"metadata\":"));
        assertTrue(json.contains("\"sources\":"));
        assertTrue(json.contains("\"system-properties\""));
        assertTrue(json.contains("\"declaredClass\": \"com.example.Test\""));
    }

    @Test
    void testIndentation() {
        List<String> items = Arrays.asList("a", "b");
        String json = JsonBuilder.buildArray(0, items);

        // Check that indentation exists
        assertTrue(json.contains("\n  \"a\""));
        assertTrue(json.contains("\n  \"b\""));
    }

    @Test
    void testEscapeInObject() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("path", "/path/to/file");
        map.put("message", "Line 1\nLine 2");

        String json = JsonBuilder.buildObject(0, map);

        assertTrue(json.contains("\\/path\\/to\\/file"));
        assertTrue(json.contains("Line 1\\nLine 2"));
    }

    @Test
    void testBuildObjectWithNullMap() {
        String json = JsonBuilder.buildObject(0, null);
        assertEquals("{}", json);
    }

    @Test
    void testBuildArrayWithNullList() {
        String json = JsonBuilder.buildArray(0, null);
        assertEquals("[]", json);
    }

    @Test
    void testEscapeBackspace() {
        String result = JsonBuilder.escape("test\bvalue");
        assertEquals("test\\bvalue", result);
    }

    @Test
    void testEscapeFormFeed() {
        String result = JsonBuilder.escape("test\fvalue");
        assertEquals("test\\fvalue", result);
    }

    @Test
    void testBuildArrayWithMixedTypes() {
        List<Object> items = new ArrayList<>();
        items.add("string");
        items.add(123);
        items.add(true);
        items.add(null);

        String json = JsonBuilder.buildArray(0, items);

        assertTrue(json.contains("\"string\""));
        assertTrue(json.contains("123"));
        assertTrue(json.contains("true"));
        assertTrue(json.contains("null"));
    }

    @Test
    void testBuildObjectWithMixedTypes() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("string", "value");
        map.put("number", 42);
        map.put("boolean", false);
        map.put("nullValue", null);

        String json = JsonBuilder.buildObject(0, map);

        assertTrue(json.contains("\"string\": \"value\""));
        assertTrue(json.contains("\"number\": 42"));
        assertTrue(json.contains("\"boolean\": false"));
        assertTrue(json.contains("\"nullValue\": null"));
    }

    @Test
    void testBuildObjectWithDeeplyNestedStructure() {
        Map<String, Object> innermost = new LinkedHashMap<>();
        innermost.put("level", 3);

        Map<String, Object> middle = new LinkedHashMap<>();
        middle.put("level", 2);
        middle.put("inner", innermost);

        Map<String, Object> outer = new LinkedHashMap<>();
        outer.put("level", 1);
        outer.put("middle", middle);

        String json = JsonBuilder.buildObject(0, outer);

        assertTrue(json.contains("\"level\": 1"));
        assertTrue(json.contains("\"level\": 2"));
        assertTrue(json.contains("\"level\": 3"));
    }

    @Test
    void testEscapeAllSpecialCharacters() {
        String input = "\"\\/\b\f\n\r\t";
        String result = JsonBuilder.escape(input);
        assertEquals("\\\"\\\\\\/\\b\\f\\n\\r\\t", result);
    }
}
