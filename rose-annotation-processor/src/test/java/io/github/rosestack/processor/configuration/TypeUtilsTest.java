package io.github.rosestack.processor.configuration;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link TypeUtils}.
 * <p>
 * Note: These tests focus on null handling and edge cases.
 * Full integration tests are performed during annotation processing.
 *
 * @author chensoul
 * @since 1.0.0
 */
class TypeUtilsTest {

    @Test
    void testGetCanonicalTypeNameWithNull() {
        // When typeMirror is null, should return default Object type
        String result = TypeUtils.getCanonicalTypeName(null, null);
        assertEquals("java.lang.Object", result);
    }

    @Test
    void testConstructorThrowsException() {
        // Verify that the utility class constructor throws an exception
        java.lang.reflect.InvocationTargetException exception = assertThrows(
                java.lang.reflect.InvocationTargetException.class,
                () -> {
                    // Use reflection to access private constructor
                    java.lang.reflect.Constructor<TypeUtils> constructor = TypeUtils.class.getDeclaredConstructor();
                    constructor.setAccessible(true);
                    constructor.newInstance();
                },
                "Utility class constructor should throw an exception");

        // Verify the cause is UnsupportedOperationException
        assertNotNull(exception.getCause(), "Exception should have a cause");
        assertTrue(
                exception.getCause() instanceof UnsupportedOperationException,
                "Cause should be UnsupportedOperationException");
        assertEquals("Utility class", exception.getCause().getMessage(), "Exception message should be 'Utility class'");
    }
}
