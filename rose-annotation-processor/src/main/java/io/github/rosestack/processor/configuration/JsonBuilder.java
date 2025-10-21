package io.github.rosestack.processor.configuration;

import java.util.List;
import java.util.Map;

/**
 * A lightweight JSON builder for generating JSON strings without external dependencies.
 * <p>
 * This class provides methods to build JSON objects and arrays with proper formatting
 * and character escaping, specifically designed for annotation processing.
 *
 * @author chensoul
 * @since 1.0.0
 */
public final class JsonBuilder {

    private static final int INDENT_SIZE = 2;
    private static final String UNICODE_PREFIX = "\\u0000".substring(0, 2);
    private static final int UNICODE_LENGTH = 4;

    private JsonBuilder() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Escape special characters in JSON string according to JSON specification.
     * <p>
     * Handles the following characters:
     * <ul>
     *   <li>Quote (") → \"</li>
     *   <li>Backslash (\) → \\</li>
     *   <li>Forward slash (/) → \/</li>
     *   <li>Backspace (\b) → \b</li>
     *   <li>Form feed (\f) → \f</li>
     *   <li>Newline (\n) → \n</li>
     *   <li>Carriage return (\r) → \r</li>
     *   <li>Tab (\t) → \t</li>
     *   <li>Control characters (< 0x20) → Unicode escape</li>
     * </ul>
     *
     * @param value the string to escape
     * @return escaped string, or the original value if null or empty
     */
    public static String escape(String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }

        StringBuilder result = new StringBuilder(value.length() + 20);
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            switch (c) {
                case '"':
                    result.append("\\\"");
                    break;
                case '\\':
                    result.append("\\\\");
                    break;
                case '/':
                    result.append("\\/");
                    break;
                case '\b':
                    result.append("\\b");
                    break;
                case '\f':
                    result.append("\\f");
                    break;
                case '\n':
                    result.append("\\n");
                    break;
                case '\r':
                    result.append("\\r");
                    break;
                case '\t':
                    result.append("\\t");
                    break;
                default:
                    if (c < ' ') {
                        result.append(toUnicodeEscape(c));
                    } else {
                        result.append(c);
                    }
            }
        }
        return result.toString();
    }

    /**
     * Build a formatted JSON object from key-value pairs.
     *
     * @param indent the indentation level
     * @param entries the key-value pairs
     * @return formatted JSON object string
     */
    public static String buildObject(int indent, Map<String, Object> entries) {
        if (entries == null || entries.isEmpty()) {
            return "{}";
        }

        StringBuilder json = new StringBuilder();
        String currentIndent = indent(indent);
        String nextIndent = indent(indent + 1);

        json.append("{\n");

        int index = 0;
        int size = entries.size();
        for (Map.Entry<String, Object> entry : entries.entrySet()) {
            json.append(nextIndent).append('"').append(escape(entry.getKey())).append("\": ");

            appendValue(json, entry.getValue(), indent + 1);

            if (index < size - 1) {
                json.append(',');
            }
            json.append('\n');
            index++;
        }

        json.append(currentIndent).append('}');
        return json.toString();
    }

    /**
     * Build a formatted JSON array from list items.
     *
     * @param indent the indentation level
     * @param items the list items
     * @return formatted JSON array string
     */
    public static String buildArray(int indent, List<?> items) {
        if (items == null || items.isEmpty()) {
            return "[]";
        }

        StringBuilder json = new StringBuilder();
        String currentIndent = indent(indent);
        String nextIndent = indent(indent + 1);

        json.append("[\n");

        int size = items.size();
        for (int i = 0; i < size; i++) {
            json.append(nextIndent);
            appendValue(json, items.get(i), indent + 1);

            if (i < size - 1) {
                json.append(',');
            }
            json.append('\n');
        }

        json.append(currentIndent).append(']');
        return json.toString();
    }

    /**
     * Append a value to the JSON builder based on its type.
     * <p>
     * Supports the following types:
     * <ul>
     *   <li>null → "null"</li>
     *   <li>String → quoted and escaped string</li>
     *   <li>Boolean/Number → raw value</li>
     *   <li>List → nested JSON array</li>
     *   <li>Map → nested JSON object</li>
     *   <li>Other → toString() quoted and escaped</li>
     * </ul>
     */
    @SuppressWarnings("unchecked")
    private static void appendValue(StringBuilder json, Object value, int indent) {
        if (value == null) {
            json.append("null");
        } else if (value instanceof String) {
            json.append('"').append(escape((String) value)).append('"');
        } else if (value instanceof Boolean || value instanceof Number) {
            json.append(value);
        } else if (value instanceof List) {
            json.append(buildArray(indent, (List<?>) value));
        } else if (value instanceof Map) {
            json.append(buildObject(indent, (Map<String, Object>) value));
        } else {
            json.append('"').append(escape(value.toString())).append('"');
        }
    }

    /**
     * Generate indentation string for the specified level.
     *
     * @param level the indentation level
     * @return indentation string (2 spaces per level)
     */
    private static String indent(int level) {
        if (level <= 0) {
            return "";
        }

        int totalSpaces = level * INDENT_SIZE;
        StringBuilder indent = new StringBuilder(totalSpaces);
        for (int i = 0; i < totalSpaces; i++) {
            indent.append(' ');
        }
        return indent.toString();
    }

    /**
     * Convert a character to Unicode escape sequence.
     *
     * @param c the character to convert
     * @return Unicode escape string (e.g., "\u0001")
     */
    private static String toUnicodeEscape(char c) {
        String hex = Integer.toHexString(c);
        StringBuilder unicode = new StringBuilder(UNICODE_PREFIX);
        for (int i = 0; i < UNICODE_LENGTH - hex.length(); i++) {
            unicode.append('0');
        }
        unicode.append(hex);
        return unicode.toString();
    }
}
