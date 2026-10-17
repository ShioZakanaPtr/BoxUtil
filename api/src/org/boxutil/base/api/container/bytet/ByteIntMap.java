package org.boxutil.base.api.container.bytet;

import org.boxutil.base.api.container.MapExt;
import org.boxutil.base.api.container.SetExt;
import org.boxutil.base.api.container.intt.IntCollection;
import org.boxutil.util.function.bytet.ByteFunction;
import org.boxutil.util.function.bytet.ByteIntBiConsumer;
import org.boxutil.util.function.bytet.ByteIntBiFunction;
import org.boxutil.util.function.bytet.ByteIntBiOperator;
import org.boxutil.util.function.intt.IntIntBiFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.function.*;

public interface ByteIntMap extends MapExt<Byte, Integer> {
    int get(byte key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer get(Object key) {
        if (key != null && !(key instanceof Byte)) return this.getDefaultReturnValueRaw();
        return this.get(key != null ? ((Byte) key).byteValue() : (byte) 0);
    }

    int getOrDefault(byte key, int defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer getOrDefault(Object key, Integer defaultValue) {
        if (key != null && !(key instanceof Byte)) return defaultValue;
        return this.getOrDefault(key != null ? ((Byte) key).byteValue() : (byte) 0, defaultValue != null ? defaultValue.intValue() : (int) 0);
    }

    int put(byte key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default @Nullable Integer put(Byte key, Integer value) {
        return this.put(key != null ? key.byteValue() : (byte) 0, value != null ? value.intValue() : (int) 0);
    }

    int putIfAbsent(byte key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer putIfAbsent(Byte key, Integer value) {
        return this.putIfAbsent(key != null ? key.byteValue() : (byte) 0, value != null ? value.intValue() : (int) 0);
    }

    int remove(byte key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer remove(Object key) {
        if (key != null && !(key instanceof Byte)) return this.getDefaultReturnValueRaw();
        return this.remove(key != null ? ((Byte) key).byteValue() : (byte) 0);
    }

    boolean remove(byte key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Byte)) return false;
        if (value != null && !(value instanceof Integer)) return false;
        return this.remove(key != null ? ((Byte) key).byteValue() : (byte) 0, value != null ? ((Integer) value).intValue() : (int) 0);
    }

    int replace(byte key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer replace(Byte key, Integer value) {
        return this.replace(key != null ? key.byteValue() : (byte) 0, value != null ? value.intValue() : (int) 0);
    }

    boolean replace(byte key, int oldValue, int newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean replace(Byte key, Integer oldValue, Integer newValue) {
        return this.replace(key != null ? key.byteValue() : (byte) 0, oldValue != null ? oldValue.intValue() : (int) 0, newValue != null ? newValue.intValue() : (int) 0);
    }

    int computeIfAbsent(byte key, @NotNull ByteFunction<? extends Integer> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer computeIfAbsent(Byte key, @NotNull Function<? super Byte, ? extends Integer> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        return this.computeIfAbsent(key != null ? key.byteValue() : (byte) 0, (byte l_t) -> mappingFunction.apply(l_t));
    }

    int computeIfPresent(byte key, @NotNull ByteIntBiFunction<? extends Integer> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer computeIfPresent(Byte key, @NotNull BiFunction<? super Byte, ? super Integer, ? extends Integer> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        return this.computeIfPresent(key != null ? key.byteValue() : (byte) 0, (byte l_t, int l_u) -> remappingFunction.apply(l_t, l_u));
    }

    int compute(byte key, @NotNull ByteIntBiFunction<? extends Integer> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer compute(Byte key, @NotNull BiFunction<? super Byte, ? super Integer, ? extends Integer> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        return this.compute(key != null ? key.byteValue() : (byte) 0, (byte l_t, int l_u) -> remappingFunction.apply(l_t, l_u));
    }

    int merge(byte key, int value, @NotNull IntIntBiFunction<? extends Integer> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer merge(Byte key, @NotNull Integer value, @NotNull BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        Objects.requireNonNull(value);
        Objects.requireNonNull(remappingFunction);
        return this.merge(key != null ? key.byteValue() : (byte) 0, value.intValue(), (int l_t, int l_u) -> remappingFunction.apply(l_t, l_u));
    }

    int update(byte key, @NotNull IntUnaryOperator updateFunction);

    @Nullable
    default Integer update(Byte key, @NotNull UnaryOperator<Integer> updateFunction) {
        return this.update(key, (int l_t) -> updateFunction.apply(l_t));
    }

    int remapping(byte key, @NotNull ByteIntBiFunction<? extends Byte> remappingFunction, IntIntBiFunction<? extends Integer> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer remapping(Byte key, @NotNull BiFunction<? super Byte, ? super Integer, ? extends Byte> remappingFunction, @NotNull BiFunction<? super Integer, ? super Integer, ? extends Integer> mergeFunction) {
        Objects.requireNonNull(remappingFunction);
        Objects.requireNonNull(mergeFunction);
        return this.remapping(key != null ? key.byteValue() : (byte) 0, (byte l_t, int l_u) -> remappingFunction.apply(l_t, l_u), (int l_t, int l_u) -> mergeFunction.apply(l_t, l_u));
    }

    boolean containsKey(byte key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean containsKey(Object key) {
        if (key != null && !(key instanceof Byte)) return false;
        return this.containsKey(key != null ? ((Byte) key).byteValue() : (byte) 0);
    }

    boolean containsValue(byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean containsValue(Object value) {
        if (value != null && !(value instanceof Integer)) return false;
        return this.containsValue(value != null ? ((Integer) value).intValue() : (int) 0);
    }

    void forEach(@NotNull ByteIntBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Byte, ? super Integer> action) {
        Objects.requireNonNull(action);
        this.forEach((byte l_t, int l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull ByteIntBiOperator function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Byte, ? super Integer, ? extends Integer> function) {
        Objects.requireNonNull(function);
        this.replaceAll((byte l_t, int l_u) -> function.apply(l_t, l_u));
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

    @NotNull ByteSet keySet();

    @NotNull IntCollection values();

    @NotNull SetExt<Entry> rawEntrySet();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default @NotNull SetExt<Map.Entry<Byte, Integer>> entrySet() {
        return (SetExt) this.rawEntrySet();
    }

    interface Entry extends Map.Entry<Byte, Integer> {
        byte getRawKey();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         */
        @Deprecated
        default Byte getKey() {
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
