package io.github.rosestack.processor.configuration;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Unit tests for {@link ConfigurationPropertyAnnotationProcessor}.
 * <p>
 * Note: These tests verify basic processor configuration and metadata.
 * Full integration tests are performed during actual annotation processing.
 *
 * @author chensoul
 * @since 1.0.0
 */
class ConfigurationPropertyAnnotationProcessorTest {

    @Test
    void testProcessorAnnotations() {
        ConfigurationPropertyAnnotationProcessor processor = new ConfigurationPropertyAnnotationProcessor();

        // Verify supported annotation types
        SupportedAnnotationTypes annotationTypes = processor.getClass().getAnnotation(SupportedAnnotationTypes.class);
        assertNotNull(annotationTypes, "Processor should have @SupportedAnnotationTypes");

        String[] types = annotationTypes.value();
        assertEquals(1, types.length, "Should support exactly one annotation type");
        assertEquals(
                "io.github.rosestack.processor.configuration.ConfigurationProperty",
                types[0],
                "Should support @ConfigurationProperty annotation");
    }

    @Test
    void testProcessorSourceVersion() {
        ConfigurationPropertyAnnotationProcessor processor = new ConfigurationPropertyAnnotationProcessor();

        // Verify supported source version
        SupportedSourceVersion sourceVersion = processor.getClass().getAnnotation(SupportedSourceVersion.class);
        assertNotNull(sourceVersion, "Processor should have @SupportedSourceVersion");

        SourceVersion version = sourceVersion.value();
        assertTrue(version.ordinal() >= SourceVersion.RELEASE_8.ordinal(), "Should support at least Java 8");
    }

    @Test
    void testGetSupportedAnnotationTypes() {
        ConfigurationPropertyAnnotationProcessor processor = new ConfigurationPropertyAnnotationProcessor();

        Set<String> supportedTypes = processor.getSupportedAnnotationTypes();
        assertNotNull(supportedTypes, "Supported annotation types should not be null");
        assertEquals(1, supportedTypes.size(), "Should support exactly one annotation type");
        assertTrue(
                supportedTypes.contains("io.github.rosestack.processor.configuration.ConfigurationProperty"),
                "Should support @ConfigurationProperty annotation");
    }

    @Test
    void testGetSupportedSourceVersion() {
        ConfigurationPropertyAnnotationProcessor processor = new ConfigurationPropertyAnnotationProcessor();

        SourceVersion version = processor.getSupportedSourceVersion();
        assertNotNull(version, "Source version should not be null");
        assertTrue(version.ordinal() >= SourceVersion.RELEASE_8.ordinal(), "Should support at least Java 8");
    }

    @Test
    void testMetadataFileGeneration(@TempDir Path tempDir) throws IOException {
        // Create a test Java source file with @ConfigurationProperty annotation
        Path sourceDir = tempDir.resolve("src");
        Path outputDir = tempDir.resolve("output");
        Files.createDirectories(sourceDir);
        Files.createDirectories(outputDir);

        // Write test source file
        String testSource = "package test;\n"
                + "import io.github.rosestack.processor.configuration.ConfigurationProperty;\n"
                + "public class TestConfig {\n"
                + "    @ConfigurationProperty(\n"
                + "        name = \"test.property\",\n"
                + "        type = String.class,\n"
                + "        defaultValue = \"test\",\n"
                + "        required = true,\n"
                + "        description = \"Test property\"\n"
                + "    )\n"
                + "    private static final String TEST_PROP = \"test\";\n"
                + "}\n";

        Path sourceFile = sourceDir.resolve("TestConfig.java");
        Files.write(sourceFile, testSource.getBytes(StandardCharsets.UTF_8));

        // Compile with annotation processor
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        assertNotNull(compiler, "Java compiler should be available");

        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager fileManager =
                compiler.getStandardFileManager(diagnostics, null, StandardCharsets.UTF_8);

        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjects(sourceFile.toFile());

        List<String> options = Arrays.asList(
                "-d",
                outputDir.toString(),
                "-proc:only", // Only run annotation processing
                "-processor",
                ConfigurationPropertyAnnotationProcessor.class.getName());

        JavaCompiler.CompilationTask task =
                compiler.getTask(null, fileManager, diagnostics, options, null, compilationUnits);

        Boolean success = task.call();
        fileManager.close();

        // Verify compilation succeeded
        assertTrue(success, "Compilation should succeed");

        // Verify metadata file was generated
        Path metadataFile = outputDir.resolve("META-INF/rose/configuration-properties.json");
        assertTrue(Files.exists(metadataFile), "Metadata file should be generated: " + metadataFile);

        // Verify file content
        String content = new String(Files.readAllBytes(metadataFile), StandardCharsets.UTF_8);
        assertFalse(content.isEmpty(), "Metadata file should not be empty");
        assertTrue(content.contains("test.property"), "Metadata should contain property name");
        assertTrue(content.contains("Test property"), "Metadata should contain description");
    }
}
