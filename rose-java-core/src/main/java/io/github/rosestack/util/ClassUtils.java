package io.github.rosestack.util;

import io.github.rosestack.annotation.Nullable;

import java.util.Objects;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since TODO
 */
public class ClassUtils {
    public static boolean isAssignableFrom(@Nullable Class<?> superType, @Nullable Class<?> targetType) {
        // any argument is null
        if (superType == null || targetType == null) {
            return false;
        }
        // equals
        if (Objects.equals(superType, targetType)) {
            return true;
        }
        // isAssignableFrom
        return superType.isAssignableFrom(targetType);
    }
}
