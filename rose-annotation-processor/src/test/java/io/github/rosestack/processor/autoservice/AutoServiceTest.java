package io.github.rosestack.processor.autoservice;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link AutoService} annotation.
 *
 * @author chensoul
 * @since 1.0.0
 */
class AutoServiceTest {

    @Test
    void testAnnotationPresent() {
        // Verify that AutoService annotation exists and can be accessed
        assertNotNull(AutoService.class, "AutoService annotation should exist");
    }

    @Test
    void testAnnotationRetention() {
        // Verify retention policy is SOURCE
        Retention retention = AutoService.class.getAnnotation(Retention.class);
        assertNotNull(retention, "AutoService should have @Retention annotation");
        assertEquals(RetentionPolicy.SOURCE, retention.value(), "AutoService retention should be SOURCE");
    }

    @Test
    void testAnnotationTarget() {
        // Verify target is TYPE
        Target target = AutoService.class.getAnnotation(Target.class);
        assertNotNull(target, "AutoService should have @Target annotation");
        assertEquals(1, target.value().length, "AutoService should target exactly one element type");
        assertEquals(java.lang.annotation.ElementType.TYPE, target.value()[0], "AutoService should target TYPE");
    }

    @Test
    void testAnnotationDocumented() {
        // Verify annotation is documented
        Documented documented = AutoService.class.getAnnotation(Documented.class);
        assertNotNull(documented, "AutoService should have @Documented annotation");
    }

    @Test
    void testAnnotationHasValueMethod() throws NoSuchMethodException {
        // Verify value() method exists and returns Class<?>[]
        java.lang.reflect.Method method = AutoService.class.getMethod("value");
        assertNotNull(method, "AutoService should have value() method");
        assertEquals(Class[].class, method.getReturnType(), "value() method should return Class<?>[]");
    }

    @Test
    void testAnnotationDefaultValue() throws NoSuchMethodException {
        // Verify default value is void.class
        java.lang.reflect.Method method = AutoService.class.getMethod("value");
        Object defaultValue = method.getDefaultValue();
        assertNotNull(defaultValue, "value() method should have a default value");
        assertTrue(defaultValue instanceof Class[], "Default value should be Class<?>[]");

        Class<?>[] defaultClasses = (Class<?>[]) defaultValue;
        assertEquals(1, defaultClasses.length, "Default value should have exactly one element");
        assertEquals(void.class, defaultClasses[0], "Default value should be void.class");
    }
}
