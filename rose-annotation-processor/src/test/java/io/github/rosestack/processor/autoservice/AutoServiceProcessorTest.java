package io.github.rosestack.processor.autoservice;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link AutoServiceProcessor}.
 * <p>
 * Note: These tests verify basic processor configuration.
 * Full integration tests are performed during actual annotation processing.
 *
 * @author chensoul
 * @since 1.0.0
 */
class AutoServiceProcessorTest {

    @Test
    void testProcessorAnnotations() {
        AutoServiceProcessor processor = new AutoServiceProcessor();

        // Verify supported annotation types
        SupportedAnnotationTypes annotationTypes = processor.getClass().getAnnotation(SupportedAnnotationTypes.class);
        assertNotNull(annotationTypes, "Processor should have @SupportedAnnotationTypes");

        String[] types = annotationTypes.value();
        assertEquals(1, types.length, "Should support exactly one annotation type");
        assertEquals(
                "io.github.rosestack.processor.autoservice.AutoService",
                types[0],
                "Should support @AutoService annotation");
    }

    @Test
    void testProcessorSourceVersion() {
        AutoServiceProcessor processor = new AutoServiceProcessor();

        // Verify supported source version annotation
        SupportedSourceVersion sourceVersion = processor.getClass().getAnnotation(SupportedSourceVersion.class);
        assertNotNull(sourceVersion, "Processor should have @SupportedSourceVersion");

        SourceVersion version = sourceVersion.value();
        assertEquals(SourceVersion.RELEASE_8, version, "Should declare support for Java 8 in annotation");
    }

    @Test
    void testGetSupportedSourceVersion() {
        AutoServiceProcessor processor = new AutoServiceProcessor();

        // Verify getSupportedSourceVersion() returns latest supported
        SourceVersion version = processor.getSupportedSourceVersion();
        assertNotNull(version, "Source version should not be null");
        assertEquals(SourceVersion.latestSupported(), version, "Should return latest supported version");
    }

    @Test
    void testGetSupportedAnnotationTypes() {
        AutoServiceProcessor processor = new AutoServiceProcessor();

        Set<String> supportedTypes = processor.getSupportedAnnotationTypes();
        assertNotNull(supportedTypes, "Supported annotation types should not be null");
        assertEquals(1, supportedTypes.size(), "Should support exactly one annotation type");
        assertTrue(
                supportedTypes.contains("io.github.rosestack.processor.autoservice.AutoService"),
                "Should support @AutoService annotation");
    }

    @Test
    void testSupportedOptions() {
        AutoServiceProcessor processor = new AutoServiceProcessor();

        // Verify supported options
        SupportedOptions options = processor.getClass().getAnnotation(SupportedOptions.class);
        assertNotNull(options, "Processor should have @SupportedOptions");

        String[] optionValues = options.value();
        assertEquals(2, optionValues.length, "Should support exactly two options");
        Set<String> optionSet = new HashSet<>(Arrays.asList(optionValues));
        assertTrue(optionSet.contains("debug"), "Should support 'debug' option");
        assertTrue(optionSet.contains("verify"), "Should support 'verify' option");
    }

    @Test
    void testGetSupportedOptions() {
        AutoServiceProcessor processor = new AutoServiceProcessor();

        Set<String> supportedOptions = processor.getSupportedOptions();
        assertNotNull(supportedOptions, "Supported options should not be null");
        assertEquals(2, supportedOptions.size(), "Should support exactly two options");
        assertTrue(supportedOptions.contains("debug"), "Should support 'debug' option");
        assertTrue(supportedOptions.contains("verify"), "Should support 'verify' option");
    }
}
