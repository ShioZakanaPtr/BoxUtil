package org.boxutil.base.api.container.shorttype;

import org.boxutil.base.api.container.MapExt;
import org.boxutil.base.api.container.SetExt;
import org.boxutil.util.function.shorttype.ShortFunction;
import org.boxutil.util.function.shorttype.ShortObjBiConsumer;
import org.boxutil.util.function.shorttype.ShortObjBiFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public interface ShortObjMap<V> extends MapExt<Short, V> {
    V get(short key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default V get(Object key) {
        if (key != null && !(key instanceof Short)) return this.getDefaultReturnValue();
        return this.get(key != null ? ((Short) key).shortValue() : (short) 0);
    }

    V getOrDefault(short key, V defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default V getOrDefault(Object key, V defaultValue) {
        if (key != null && !(key instanceof Short)) return defaultValue;
        return this.getOrDefault(key != null ? ((Short) key).shortValue() : (short) 0, defaultValue);
    }

    V put(short key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default @Nullable V put(Short key, V value) {
        return this.put(key != null ? key.shortValue() : (short) 0, value);
    }

    V putIfAbsent(short key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default V putIfAbsent(Short key, V value) {
        return this.putIfAbsent(key != null ? key.shortValue() : (short) 0, value);
    }

    V remove(short key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default V remove(Object key) {
        if (key != null && !(key instanceof Short)) return this.getDefaultReturnValue();
        return this.remove(key != null ? ((Short) key).shortValue() : (short) 0);
    }

    boolean remove(short key, Object value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Short)) return false;
        return this.remove(key != null ? ((Short) key).shortValue() : (short) 0, value);
    }

    V replace(short key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default V replace(Short key, V value) {
        return this.replace(key != null ? key.shortValue() : (short) 0, value);
    }

    boolean replace(short key, V oldValue, V newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean replace(Short key, V oldValue, V newValue) {
        return this.replace(key != null ? key.shortValue() : (short) 0, oldValue, newValue);
    }

    V computeIfAbsent(short key, @NotNull ShortFunction<? extends V> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default V computeIfAbsent(Short key, @NotNull Function<? super Short, ? extends V> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        return this.computeIfAbsent(key != null ? key.shortValue() : (short) 0, (short l_t) -> mappingFunction.apply(l_t));
    }

    V computeIfPresent(short key, @NotNull ShortObjBiFunction<? super V, ? extends V> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default V computeIfPresent(Short key, @NotNull BiFunction<? super Short, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        return this.computeIfPresent(key != null ? key.shortValue() : (short) 0, (short l_t, V l_u) -> remappingFunction.apply(l_t, l_u));
    }

    V compute(short key, @NotNull ShortObjBiFunction<? super V, ? extends V> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default V compute(Short key, @NotNull BiFunction<? super Short, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        return this.compute(key != null ? key.shortValue() : (short) 0, (short l_t, V l_u) -> remappingFunction.apply(l_t, l_u));
    }

    V merge(short key, @NotNull V value, @NotNull BiFunction<? super V, ? super V, ? extends V> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default V merge(Short key, @NotNull V value, @NotNull BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(value);
        Objects.requireNonNull(remappingFunction);
        return this.merge(key != null ? key.shortValue() : (short) 0, value, (V l_t, V l_u) -> remappingFunction.apply(l_t, l_u));
    }

    V update(short key, @NotNull UnaryOperator<V> updateFunction);

    @Nullable
    default V update(Short key, @NotNull UnaryOperator<V> updateFunction) {
        return this.update(key != null ? key.shortValue() : (short) 0, updateFunction);
    }

    V remapping(short key, ShortObjBiFunction<? super V, ? extends Short> remappingFunction, BiFunction<? super V, ? super V, ? extends V> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default V remapping(Short key, @NotNull BiFunction<? super Short, ? super V, ? extends Short> remappingFunction, @NotNull BiFunction<? super V, ? super V, ? extends V> mergeFunction) {
        Objects.requireNonNull(remappingFunction);
        Objects.requireNonNull(mergeFunction);
        return this.remapping(key != null ? key.shortValue() : (short) 0, (short l_t, V l_u) -> remappingFunction.apply(l_t, l_u), (V l_t, V l_u) -> mergeFunction.apply(l_t, l_u));
    }

    boolean containsKey(short key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean containsKey(Object key) {
        if (key != null && !(key instanceof Short)) return false;
        return this.containsKey(key != null ? ((Short) key).shortValue() : (short) 0);
    }

    void forEach(ShortObjBiConsumer<? super V> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void forEach(BiConsumer<? super Short, ? super V> action) {
        Objects.requireNonNull(action);
        this.forEach((short l_t, V l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(ShortObjBiFunction<? super V, ? extends V> function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void replaceAll(BiFunction<? super Short, ? super V, ? extends V> function) {
        Objects.requireNonNull(function);
        this.replaceAll((short l_t, V l_u) -> function.apply(l_t, l_u));
    }

    @NotNull ShortSet keySet();

    @NotNull SetExt<Entry<V>> rawEntrySet();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default @NotNull SetExt<Map.Entry<Short, V>> entrySet() {
        return (SetExt) this.rawEntrySet();
    }

    interface Entry<V> extends Map.Entry<Short, V> {
        short getRawKey();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         */
        @Deprecated
        default Short getKey() {
            return this.getRawKey();
        }
    }
}
