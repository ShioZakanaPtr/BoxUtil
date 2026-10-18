package org.boxutil.base.api.container.objecttype;

import org.boxutil.base.api.container.MapExt;
import org.boxutil.base.api.container.SetExt;
import org.boxutil.base.api.container.inttype.IntCollection;
import org.boxutil.util.function.inttype.IntIntBiFunction;
import org.boxutil.util.function.objecttype.ObjIntBiConsumer;
import org.boxutil.util.function.objecttype.ObjIntBiFunction;
import org.boxutil.util.function.objecttype.ObjIntBiOperator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.function.*;

public interface ObjIntMap<K> extends MapExt<K, Integer> {
    int getRaw(Object key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer get(Object key) {
        return this.getRaw(key);
    }

    int getOrDefault(Object key, int defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer getOrDefault(Object key, Integer defaultValue) {
        return this.getOrDefault(key, defaultValue != null ? defaultValue.intValue() : (int) 0);
    }

    int put(K key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default @Nullable Integer put(K key, Integer value) {
        return this.put(key, value != null ? value.intValue() : (int) 0);
    }

    int putIfAbsent(K key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer putIfAbsent(K key, Integer value) {
        return this.putIfAbsent(key, value != null ? value.intValue() : (int) 0);
    }

    int removeRaw(Object key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer remove(Object key) {
        return this.removeRaw(key);
    }

    boolean remove(Object key, int value);

    default boolean remove(Object key, Object value) {
        if (value != null && !(value instanceof Integer)) return false;
        return this.remove(key, value != null ? ((Integer) value).intValue() : (int) 0);
    }

    int replace(K key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer replace(K key, Integer value) {
        return this.replace(key, value != null ? value.intValue() : (int) 0);
    }

    boolean replace(K key, int oldValue, int newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean replace(K key, Integer oldValue, Integer newValue) {
        return this.replace(key, oldValue != null ? oldValue.intValue() : (int) 0, newValue != null ? newValue.intValue() : (int) 0);
    }

    int computeIfAbsentRaw(K key, @NotNull Function<? super K, ? extends Integer> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer computeIfAbsent(K key, @NotNull Function<? super K, ? extends Integer> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        return this.computeIfAbsentRaw(key, (K l_t) -> mappingFunction.apply(l_t));
    }

    int computeIfPresent(K key, @NotNull ObjIntBiFunction<? super K, ? extends Integer> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer computeIfPresent(K key, @NotNull BiFunction<? super K, ? super Integer, ? extends Integer> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        return this.computeIfPresent(key, (K l_t, int l_u) -> remappingFunction.apply(l_t, l_u));
    }

    int compute(K key, @NotNull ObjIntBiFunction<? super K, ? extends Integer> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer compute(K key, @NotNull BiFunction<? super K, ? super Integer, ? extends Integer> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        return this.compute(key, (K l_t, int l_u) -> remappingFunction.apply(l_t, l_u));
    }

    int merge(K key, int value, @NotNull IntIntBiFunction<? extends Integer> remappingFunction);

    int update(K key, @NotNull IntUnaryOperator updateFunction);

    @Nullable
    default Integer update(K key, @NotNull UnaryOperator<Integer> updateFunction) {
        return this.update(key, (int l_t) -> updateFunction.apply(l_t));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer merge(K key, @NotNull Integer value, @NotNull BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        Objects.requireNonNull(value);
        Objects.requireNonNull(remappingFunction);
        return this.merge(key, value.intValue(), (int l_t, int l_u) -> remappingFunction.apply(l_t, l_u));
    }

    int remapping(K key, @NotNull ObjIntBiFunction<? super K, ? extends K> remappingFunction, IntIntBiFunction<? extends Integer> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer remapping(K key, @NotNull BiFunction<? super K, ? super Integer, ? extends K> remappingFunction, @NotNull BiFunction<? super Integer, ? super Integer, ? extends Integer> mergeFunction) {
        Objects.requireNonNull(remappingFunction);
        Objects.requireNonNull(mergeFunction);
        return this.remapping(key, (K l_t, int l_u) -> remappingFunction.apply(l_t, l_u), (int l_t, int l_u) -> mergeFunction.apply(l_t, l_u));
    }

    boolean containsValue(int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean containsValue(Object value) {
        if (value != null && !(value instanceof Integer)) return false;
        return this.containsValue(value != null ? ((Integer) value).intValue() : (int) 0);
    }

    void forEach(ObjIntBiConsumer<? super K> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void forEach(BiConsumer<? super K, ? super Integer> action) {
        Objects.requireNonNull(action);
        this.forEach((K l_t, int l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(ObjIntBiOperator<? super K> function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void replaceAll(BiFunction<? super K, ? super Integer, ? extends Integer> function) {
        Objects.requireNonNull(function);
        this.replaceAll((K l_t, int l_u) -> function.apply(l_t, l_u));
    }

    int getDefaultReturnValueRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer getDefaultReturnValue() {
        return this.getDefaultReturnValueRaw();
    }

    int setDefaultReturnValue(int newDefault);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer setDefaultReturnValue(Integer newDefault) {
        return this.setDefaultReturnValue(newDefault != null ? newDefault.intValue() : (int) 0);
    }

    @NotNull IntCollection values();

    @NotNull SetExt<Entry<K>> rawEntrySet();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default @NotNull SetExt<Map.Entry<K, Integer>> entrySet() {
        return (SetExt) this.rawEntrySet();
    }

    interface Entry<K> extends Map.Entry<K, Integer> {
        int getRawValue();

        int setValue(int newValue);

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         */
        @Deprecated
        default Integer getValue() {
            return this.getRawValue();
        }

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         */
        @Deprecated
        default Integer setValue(Integer value) {
            return this.setValue(value != null ? value.intValue() : (int) 0);
        }
    }
}
