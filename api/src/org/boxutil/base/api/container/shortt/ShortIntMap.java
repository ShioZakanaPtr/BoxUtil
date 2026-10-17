package org.boxutil.base.api.container.shortt;

import org.boxutil.base.api.container.MapExt;
import org.boxutil.base.api.container.SetExt;
import org.boxutil.base.api.container.intt.IntCollection;
import org.boxutil.util.function.intt.IntIntBiFunction;
import org.boxutil.util.function.shortt.ShortFunction;
import org.boxutil.util.function.shortt.ShortIntBiConsumer;
import org.boxutil.util.function.shortt.ShortIntBiFunction;
import org.boxutil.util.function.shortt.ShortIntBiOperator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.function.*;

public interface ShortIntMap extends MapExt<Short, Integer> {
    int get(short key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer get(Object key) {
        if (key != null && !(key instanceof Short)) return this.getDefaultReturnValueRaw();
        return this.get(key != null ? ((Short) key).shortValue() : (short) 0);
    }

    int getOrDefault(short key, int defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer getOrDefault(Object key, Integer defaultValue) {
        if (key != null && !(key instanceof Short)) return defaultValue;
        return this.getOrDefault(key != null ? ((Short) key).shortValue() : (short) 0, defaultValue != null ? defaultValue.intValue() : (int) 0);
    }

    int put(short key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default @Nullable Integer put(Short key, Integer value) {
        return this.put(key != null ? key.shortValue() : (short) 0, value != null ? value.intValue() : (int) 0);
    }

    int putIfAbsent(short key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer putIfAbsent(Short key, Integer value) {
        return this.putIfAbsent(key != null ? key.shortValue() : (short) 0, value != null ? value.intValue() : (int) 0);
    }

    int remove(short key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer remove(Object key) {
        if (key != null && !(key instanceof Short)) return this.getDefaultReturnValueRaw();
        return this.remove(key != null ? ((Short) key).shortValue() : (short) 0);
    }

    boolean remove(short key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Short)) return false;
        if (value != null && !(value instanceof Integer)) return false;
        return this.remove(key != null ? ((Short) key).shortValue() : (short) 0, value != null ? ((Integer) value).intValue() : (int) 0);
    }

    int replace(short key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer replace(Short key, Integer value) {
        return this.replace(key != null ? key.shortValue() : (short) 0, value != null ? value.intValue() : (int) 0);
    }

    boolean replace(short key, int oldValue, int newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean replace(Short key, Integer oldValue, Integer newValue) {
        return this.replace(key != null ? key.shortValue() : (short) 0, oldValue != null ? oldValue.intValue() : (int) 0, newValue != null ? newValue.intValue() : (int) 0);
    }

    int computeIfAbsent(short key, @NotNull ShortFunction<? extends Integer> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer computeIfAbsent(Short key, @NotNull Function<? super Short, ? extends Integer> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        return this.computeIfAbsent(key != null ? key.shortValue() : (short) 0, (short l_t) -> mappingFunction.apply(l_t));
    }

    int computeIfPresent(short key, @NotNull ShortIntBiFunction<? extends Integer> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer computeIfPresent(Short key, @NotNull BiFunction<? super Short, ? super Integer, ? extends Integer> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        return this.computeIfPresent(key != null ? key.shortValue() : (short) 0, (short l_t, int l_u) -> remappingFunction.apply(l_t, l_u));
    }

    int compute(short key, @NotNull ShortIntBiFunction<? extends Integer> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer compute(Short key, @NotNull BiFunction<? super Short, ? super Integer, ? extends Integer> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        return this.compute(key != null ? key.shortValue() : (short) 0, (short l_t, int l_u) -> remappingFunction.apply(l_t, l_u));
    }

    int merge(short key, int value, @NotNull IntIntBiFunction<? extends Integer> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer merge(Short key, @NotNull Integer value, @NotNull BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        Objects.requireNonNull(value);
        Objects.requireNonNull(remappingFunction);
        return this.merge(key != null ? key.shortValue() : (short) 0, value.intValue(), (int l_t, int l_u) -> remappingFunction.apply(l_t, l_u));
    }

    int update(short key, @NotNull IntUnaryOperator updateFunction);

    @Nullable
    default Integer update(Short key, @NotNull UnaryOperator<Integer> updateFunction) {
        return this.update(key, (int l_t) -> updateFunction.apply(l_t));
    }

    int remapping(short key, @NotNull ShortIntBiFunction<? extends Short> remappingFunction, IntIntBiFunction<? extends Integer> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer remapping(Short key, @NotNull BiFunction<? super Short, ? super Integer, ? extends Short> remappingFunction, @NotNull BiFunction<? super Integer, ? super Integer, ? extends Integer> mergeFunction) {
        Objects.requireNonNull(remappingFunction);
        Objects.requireNonNull(mergeFunction);
        return this.remapping(key != null ? key.shortValue() : (short) 0, (short l_t, int l_u) -> remappingFunction.apply(l_t, l_u), (int l_t, int l_u) -> mergeFunction.apply(l_t, l_u));
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

    boolean containsValue(short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean containsValue(Object value) {
        if (value != null && !(value instanceof Integer)) return false;
        return this.containsValue(value != null ? ((Integer) value).intValue() : (int) 0);
    }

    void forEach(@NotNull ShortIntBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Short, ? super Integer> action) {
        Objects.requireNonNull(action);
        this.forEach((short l_t, int l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull ShortIntBiOperator function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Short, ? super Integer, ? extends Integer> function) {
        Objects.requireNonNull(function);
        this.replaceAll((short l_t, int l_u) -> function.apply(l_t, l_u));
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

    @NotNull ShortSet keySet();

    @NotNull IntCollection values();

    @NotNull SetExt<Entry> rawEntrySet();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default @NotNull SetExt<Map.Entry<Short, Integer>> entrySet() {
        return (SetExt) this.rawEntrySet();
    }

    interface Entry extends Map.Entry<Short, Integer> {
        short getRawKey();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         */
        @Deprecated
        default Short getKey() {
            return this.getRawKey();
        }

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
