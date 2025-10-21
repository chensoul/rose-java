package io.github.rosestack.processor.configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

/**
 * Annotation processor for {@link ConfigurationProperty}.
 * <p>
 * This processor generates a JSON file containing metadata for all fields
 * annotated with {@link ConfigurationProperty} during compilation.
 * <p>
 * The generated file is located at: {@code META-INF/rose/configuration-properties.json}
 *
 * @author chensoul
 * @since 1.0.0
 */
@SupportedAnnotationTypes("io.github.rosestack.processor.configuration.ConfigurationProperty")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ConfigurationPropertyAnnotationProcessor extends AbstractProcessor {

    private static final String OUTPUT_FILE = "META-INF/rose/configuration-properties.json";

    private Messager messager;
    private Filer filer;
    private Elements elementUtils;
    private Types typeUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.messager = processingEnv.getMessager();
        this.filer = processingEnv.getFiler();
        this.elementUtils = processingEnv.getElementUtils();
        this.typeUtils = processingEnv.getTypeUtils();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            return false;
        }

        try {
            // Collect all configuration properties
            Set<ConfigurationPropertyMetadata> properties = collectConfigurationProperties(roundEnv);

            if (properties.isEmpty()) {
                return false;
            }

            // Load existing properties from previous compilation rounds
            Set<ConfigurationPropertyMetadata> existingProperties = loadExistingProperties();

            // Merge with existing properties
            properties.addAll(existingProperties);

            // Generate JSON file
            generateJsonFile(properties);

            messager.printMessage(
                    Diagnostic.Kind.NOTE,
                    String.format("Generated %s with %d configuration properties", OUTPUT_FILE, properties.size()));

        } catch (Exception e) {
            messager.printMessage(
                    Diagnostic.Kind.ERROR, "Failed to process @ConfigurationProperty annotations: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Collect all configuration properties from annotated fields.
     */
    private Set<ConfigurationPropertyMetadata> collectConfigurationProperties(RoundEnvironment roundEnv) {
        Set<ConfigurationPropertyMetadata> properties = new LinkedHashSet<>();

        for (Element element : roundEnv.getElementsAnnotatedWith(ConfigurationProperty.class)) {
            if (!(element instanceof VariableElement)) {
                messager.printMessage(
                        Diagnostic.Kind.WARNING, "@ConfigurationProperty can only be applied to fields", element);
                continue;
            }

            VariableElement field = (VariableElement) element;
            ConfigurationProperty annotation = field.getAnnotation(ConfigurationProperty.class);

            if (annotation == null) {
                continue;
            }

            try {
                ConfigurationPropertyMetadata metadata = createMetadata(field, annotation);
                properties.add(metadata);
            } catch (Exception e) {
                messager.printMessage(
                        Diagnostic.Kind.ERROR,
                        "Failed to process field: " + field.getSimpleName() + " - " + e.getMessage(),
                        field);
            }
        }

        return properties;
    }

    /**
     * Create metadata from field and annotation.
     */
    private ConfigurationPropertyMetadata createMetadata(VariableElement field, ConfigurationProperty annotation) {
        // Get field name
        String fieldName = field.getSimpleName().toString();

        // Get property name (use annotation value or field name)
        String name = annotation.name();
        if (name == null || name.isEmpty()) {
            name = fieldName;
        }

        // Get field type
        TypeMirror fieldType = field.asType();
        String type = TypeUtils.getCanonicalTypeName(fieldType, typeUtils);

        // Get annotation type (if different from field type)
        // Note: Accessing annotation.type() throws MirroredTypeException in annotation processing
        try {
            Class<?> annotationType = annotation.type();
            if (annotationType != null && !annotationType.equals(String.class)) {
                type = annotationType.getName();
            }
        } catch (MirroredTypeException mte) {
            // This is expected behavior in annotation processing
            TypeMirror typeMirror = mte.getTypeMirror();
            String annotationType = TypeUtils.getCanonicalTypeName(typeMirror, typeUtils);
            if (annotationType != null && !annotationType.equals("java.lang.String")) {
                type = annotationType;
            }
        }

        // Get default value
        String defaultValue = annotation.defaultValue();

        // Get required flag
        boolean required = annotation.required();

        // Get description
        String description = annotation.description();

        // Get sources
        String[] sources = annotation.source();
        if (sources == null || sources.length == 0) {
            sources = new String[] {ConfigurationProperty.SYSTEM_PROPERTIES_SOURCE};
        }

        // Get declared class
        Element enclosingElement = field.getEnclosingElement();
        String declaredClass =
                elementUtils.getBinaryName((TypeElement) enclosingElement).toString();

        return new ConfigurationPropertyMetadata(
                name, type, defaultValue, required, description, sources, declaredClass, fieldName);
    }

    /**
     * Load existing properties from previous compilation rounds.
     */
    private Set<ConfigurationPropertyMetadata> loadExistingProperties() {
        Set<ConfigurationPropertyMetadata> properties = new LinkedHashSet<>();

        try {
            FileObject resource = filer.getResource(StandardLocation.CLASS_OUTPUT, "", OUTPUT_FILE);
            try (BufferedReader reader =
                    new BufferedReader(new InputStreamReader(resource.openInputStream(), StandardCharsets.UTF_8))) {
                // Note: We're not parsing the existing JSON here to avoid complexity
                // In a real implementation, you might want to parse and merge properly
                // For now, we'll just overwrite with new properties
            }
        } catch (IOException e) {
            // File doesn't exist yet, which is fine
            messager.printMessage(Diagnostic.Kind.NOTE, "No existing configuration properties file found");
        }

        return properties;
    }

    /**
     * Generate JSON file with all configuration properties.
     */
    private void generateJsonFile(Set<ConfigurationPropertyMetadata> properties) throws IOException {
        FileObject resource = filer.createResource(StandardLocation.CLASS_OUTPUT, "", OUTPUT_FILE);

        try (Writer writer = resource.openWriter()) {
            // Convert properties to JSON array
            List<Map<String, Object>> jsonList = new ArrayList<>();
            for (ConfigurationPropertyMetadata property : properties) {
                jsonList.add(property.toJsonMap());
            }

            String json = JsonBuilder.buildArray(0, jsonList);
            writer.write(json);
            writer.write('\n');
        }
    }
}
