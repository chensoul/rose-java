package io.github.rosestack.processor.configuration;

import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;

/**
 * Type utility class for annotation processing.
 *
 * @author chensoul
 * @since 1.0.0
 */
public final class TypeUtils {

    private TypeUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Get the canonical name of a type, including array dimensions.
     *
     * @param typeMirror the type mirror
     * @param types the Types utility
     * @return the canonical type name (e.g., "java.lang.String[]")
     */
    public static String getCanonicalTypeName(TypeMirror typeMirror, Types types) {
        if (typeMirror == null) {
            return "java.lang.Object";
        }

        TypeKind kind = typeMirror.getKind();

        // Handle primitive types
        if (kind.isPrimitive()) {
            return getPrimitiveTypeName((PrimitiveType) typeMirror);
        }

        // Handle array types
        if (kind == TypeKind.ARRAY) {
            ArrayType arrayType = (ArrayType) typeMirror;
            TypeMirror componentType = arrayType.getComponentType();
            return getCanonicalTypeName(componentType, types) + "[]";
        }

        // Handle declared types (classes, interfaces)
        if (kind == TypeKind.DECLARED) {
            DeclaredType declaredType = (DeclaredType) typeMirror;
            return declaredType.asElement().toString();
        }

        // Handle void
        if (kind == TypeKind.VOID) {
            return "void";
        }

        // Fallback to toString()
        return typeMirror.toString();
    }

    /**
     * Get primitive type name.
     */
    private static String getPrimitiveTypeName(PrimitiveType primitiveType) {
        switch (primitiveType.getKind()) {
            case BOOLEAN:
                return "boolean";
            case BYTE:
                return "byte";
            case SHORT:
                return "short";
            case INT:
                return "int";
            case LONG:
                return "long";
            case CHAR:
                return "char";
            case FLOAT:
                return "float";
            case DOUBLE:
                return "double";
            default:
                return primitiveType.toString();
        }
    }
}
