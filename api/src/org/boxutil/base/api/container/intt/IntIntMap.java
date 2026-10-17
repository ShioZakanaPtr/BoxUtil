package org.boxutil.base.api.container.intt;

import org.boxutil.base.api.container.MapExt;
import org.boxutil.base.api.container.SetExt;
import org.boxutil.util.function.intt.IntIntBiConsumer;
import org.boxutil.util.function.intt.IntIntBiFunction;
import org.boxutil.util.function.intt.IntIntBiOperator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.function.*;

public interface IntIntMap extends MapExt<Integer, Integer> {
    int get(int key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer get(Object key) {
        if (key != null && !(key instanceof Integer)) return this.getDefaultReturnValueRaw();
        return this.get(key != null ? ((Integer) key).intValue() : (int) 0);
    }

    int getOrDefault(int key, int defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer getOrDefault(Object key, Integer defaultValue) {
        if (key != null && !(key instanceof Integer)) return defaultValue;
        return this.getOrDefault(key != null ? ((Integer) key).intValue() : (int) 0, defaultValue != null ? defaultValue.intValue() : (int) 0);
    }

    int put(int key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default @Nullable Integer put(Integer key, Integer value) {
        return this.put(key != null ? key.intValue() : (int) 0, value != null ? value.intValue() : (int) 0);
    }

    int putIfAbsent(int key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer putIfAbsent(Integer key, Integer value) {
        return this.putIfAbsent(key != null ? key.intValue() : (int) 0, value != null ? value.intValue() : (int) 0);
    }

    int remove(int key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer remove(Object key) {
        if (key != null && !(key instanceof Integer)) return this.getDefaultReturnValueRaw();
        return this.remove(key != null ? ((Integer) key).intValue() : (int) 0);
    }

    boolean remove(int key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Integer)) return false;
        if (value != null && !(value instanceof Integer)) return false;
        return this.remove(key != null ? ((Integer) key).intValue() : (int) 0, value != null ? ((Integer) value).intValue() : (int) 0);
    }

    int replace(int key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer replace(Integer key, Integer value) {
        return this.replace(key != null ? key.intValue() : (int) 0, value != null ? value.intValue() : (int) 0);
    }

    boolean replace(int key, int oldValue, int newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean replace(Integer key, Integer oldValue, Integer newValue) {
        return this.replace(key != null ? key.intValue() : (int) 0, oldValue != null ? oldValue.intValue() : (int) 0, newValue != null ? newValue.intValue() : (int) 0);
    }

    int computeIfAbsent(int key, @NotNull IntFunction<? extends Integer> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer computeIfAbsent(Integer key, @NotNull Function<? super Integer, ? extends Integer> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        return this.computeIfAbsent(key != null ? key.intValue() : (int) 0, (int l_t) -> mappingFunction.apply(l_t));
    }

    int computeIfPresent(int key, @NotNull IntIntBiFunction<? extends Integer> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer computeIfPresent(Integer key, @NotNull BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        return this.computeIfPresent(key != null ? key.intValue() : (int) 0, (int l_t, int l_u) -> remappingFunction.apply(l_t, l_u));
    }

    int compute(int key, @NotNull IntIntBiFunction<? extends Integer> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer compute(Integer key, @NotNull BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        return this.compute(key != null ? key.intValue() : (int) 0, (int l_t, int l_u) -> remappingFunction.apply(l_t, l_u));
    }

    int merge(int key, int value, @NotNull IntIntBiFunction<? extends Integer> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer merge(Integer key, @NotNull Integer value, @NotNull BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        Objects.requireNonNull(value);
        Objects.requireNonNull(remappingFunction);
        return this.merge(key != null ? key.intValue() : (int) 0, value.intValue(), (int l_t, int l_u) -> remappingFunction.apply(l_t, l_u));
    }

    int update(int key, @NotNull IntUnaryOperator updateFunction);

    @Nullable
    default Integer update(Integer key, @NotNull UnaryOperator<Integer> updateFunction) {
        return this.update(key, (int l_t) -> updateFunction.apply(l_t));
    }

    int remapping(int key, @NotNull IntIntBiFunction<? extends Integer> remappingFunction, IntIntBiFunction<? extends Integer> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer remapping(Integer key, @NotNull BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction, @NotNull BiFunction<? super Integer, ? super Integer, ? extends Integer> mergeFunction) {
        Objects.requireNonNull(remappingFunction);
        Objects.requireNonNull(mergeFunction);
        return this.remapping(key != null ? key.intValue() : (int) 0, (int l_t, int l_u) -> remappingFunction.apply(l_t, l_u), (int l_t, int l_u) -> mergeFunction.apply(l_t, l_u));
    }

    boolean containsKey(int key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean containsKey(Object key) {
        if (key != null && !(key instanceof Integer)) return false;
        return this.containsKey(key != null ? ((Integer) key).intValue() : (int) 0);
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

    void forEach(@NotNull IntIntBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Integer, ? super Integer> action) {
        Objects.requireNonNull(action);
        this.forEach((int l_t, int l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull IntIntBiOperator function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Integer, ? super Integer, ? extends Integer> function) {
        Objects.requireNonNull(function);
        this.replaceAll((int l_t, int l_u) -> function.apply(l_t, l_u));
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

    @NotNull IntSet keySet();

    @NotNull IntCollection values();

    @NotNull SetExt<Entry> rawEntrySet();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default @NotNull SetExt<Map.Entry<Integer, Integer>> entrySet() {
        return (SetExt) this.rawEntrySet();
    }

    interface Entry extends Map.Entry<Integer, Integer> {
        int getRawKey();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         */
        @Deprecated
        default Integer getKey() {
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
