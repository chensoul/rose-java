package io.github.rosestack.util;

import io.github.rosestack.annotation.Immutable;
import io.github.rosestack.annotation.Nonnull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since TODO
 */
public class MapUtils {
    public static final float MIN_LOAD_FACTOR = 0.75f;

    public static boolean isMap(Object instance) {
        return instance instanceof Map;
    }

    public static boolean isMap(Class<?> type) {
        return ClassUtils.isAssignableFrom(Map.class, type);
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static int size(Map<?, ?> map) {
        return map == null ? 0 : map.size();
    }

    @Nonnull
    @Immutable
    public static <K, V> Map<K, V> of(K key, V value) {
        return ofMap(key, value);
    }

    @Nonnull
    @Immutable
    public static <K, V> Map<K, V> of(K key1, V value1, K key2, V value2) {
        return ofMap(key1, value1, key2, value2);
    }

    @Nonnull
    @Immutable
    public static <K, V> Map<K, V> of(K key1, V value1, K key2, V value2, K key3, V value3) {
        return ofMap(key1, value1, key2, value2, key3, value3);
    }

    @Nonnull
    @Immutable
    public static <K, V> Map<K, V> of(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4) {
        return ofMap(key1, value1, key2, value2, key3, value3, key4, value4);
    }

    @Nonnull
    @Immutable
    public static <K, V> Map<K, V> of(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5) {
        return ofMap(key1, value1, key2, value2, key3, value3, key4, value4, key5, value5);
    }

    @Nonnull
    @Immutable
    public static Map of(Object... values) {
        return ofMap(values);
    }

    @Nonnull
    @Immutable
    public static <K, V> Map<K, V> of(Map.Entry<? extends K, ? extends V>... entries) {
        int length = ArrayUtils.length(entries);
        if (length < 1) {
            return Collections.emptyMap();
        } else {
            Map<K, V> map = new LinkedHashMap<>(length);

            for (int i = 0; i < length; ++i) {
                Map.Entry<? extends K, ? extends V> entry = entries[i];
                map.put(entry.getKey(), entry.getValue());
            }

            return Collections.unmodifiableMap(map);
        }
    }

    @Nonnull
    @Immutable
    public static <K, V> Map<K, V> ofMap(K key, V value) {
        return Collections.singletonMap(key, value);
    }

    @Nonnull
    @Immutable
    public static Map ofMap(Object... keyValuePairs) {
        int length = ArrayUtils.length(keyValuePairs);
        if (length < 1) {
            return Collections.emptyMap();
        } else {
            int size = length / 2;
            Map map = new LinkedHashMap(size);
            int i = 0;

            while (i < length) {
                map.put(keyValuePairs[i++], keyValuePairs[i++]);
            }

            return Collections.unmodifiableMap(map);
        }
    }

    @Nonnull
    public static <K, V> Map<K, V> shallowCloneMap(@Nonnull Map<K, V> source) {
        if (source instanceof LinkedHashMap) {
            return new LinkedHashMap(source);
        } else if (source instanceof ConcurrentNavigableMap) {
            return new ConcurrentSkipListMap(source);
        } else if (source instanceof SortedMap) {
            return new TreeMap(source);
        } else if (source instanceof ConcurrentMap) {
            return new ConcurrentHashMap(source);
        } else {
            return (Map<K, V>) (source instanceof IdentityHashMap ? new IdentityHashMap(source) : new HashMap(source));
        }
    }
}
