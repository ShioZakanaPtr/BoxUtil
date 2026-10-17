package org.boxutil.base.api.container.bytet;

import org.boxutil.base.api.container.MapExt;
import org.boxutil.base.api.container.SetExt;
import org.boxutil.base.api.container.longt.LongCollection;
import org.boxutil.util.function.bytet.ByteFunction;
import org.boxutil.util.function.bytet.ByteLongBiConsumer;
import org.boxutil.util.function.bytet.ByteLongBiFunction;
import org.boxutil.util.function.bytet.ByteLongBiOperator;
import org.boxutil.util.function.longt.LongLongBiFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.function.*;

public interface ByteLongMap extends MapExt<Byte, Long> {
    long get(byte key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long get(Object key) {
        if (key != null && !(key instanceof Byte)) return this.getDefaultReturnValueRaw();
        return this.get(key != null ? ((Byte) key).byteValue() : (byte) 0);
    }

    long getOrDefault(byte key, long defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long getOrDefault(Object key, Long defaultValue) {
        if (key != null && !(key instanceof Byte)) return defaultValue;
        return this.getOrDefault(key != null ? ((Byte) key).byteValue() : (byte) 0, defaultValue != null ? defaultValue.longValue() : (long) 0);
    }

    long put(byte key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default @Nullable Long put(Byte key, Long value) {
        return this.put(key != null ? key.byteValue() : (byte) 0, value != null ? value.longValue() : (long) 0);
    }

    long putIfAbsent(byte key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long putIfAbsent(Byte key, Long value) {
        return this.putIfAbsent(key != null ? key.byteValue() : (byte) 0, value != null ? value.longValue() : (long) 0);
    }

    long remove(byte key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long remove(Object key) {
        if (key != null && !(key instanceof Byte)) return this.getDefaultReturnValueRaw();
        return this.remove(key != null ? ((Byte) key).byteValue() : (byte) 0);
    }

    boolean remove(byte key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Byte)) return false;
        if (value != null && !(value instanceof Long)) return false;
        return this.remove(key != null ? ((Byte) key).byteValue() : (byte) 0, value != null ? ((Long) value).longValue() : (long) 0);
    }

    long replace(byte key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long replace(Byte key, Long value) {
        return this.replace(key != null ? key.byteValue() : (byte) 0, value != null ? value.longValue() : (long) 0);
    }

    boolean replace(byte key, long oldValue, long newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean replace(Byte key, Long oldValue, Long newValue) {
        return this.replace(key != null ? key.byteValue() : (byte) 0, oldValue != null ? oldValue.longValue() : (long) 0, newValue != null ? newValue.longValue() : (long) 0);
    }

    long computeIfAbsent(byte key, @NotNull ByteFunction<? extends Long> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long computeIfAbsent(Byte key, @NotNull Function<? super Byte, ? extends Long> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        return this.computeIfAbsent(key != null ? key.byteValue() : (byte) 0, (byte l_t) -> mappingFunction.apply(l_t));
    }

    long computeIfPresent(byte key, @NotNull ByteLongBiFunction<? extends Long> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long computeIfPresent(Byte key, @NotNull BiFunction<? super Byte, ? super Long, ? extends Long> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        return this.computeIfPresent(key != null ? key.byteValue() : (byte) 0, (byte l_t, long l_u) -> remappingFunction.apply(l_t, l_u));
    }

    long compute(byte key, @NotNull ByteLongBiFunction<? extends Long> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long compute(Byte key, @NotNull BiFunction<? super Byte, ? super Long, ? extends Long> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        return this.compute(key != null ? key.byteValue() : (byte) 0, (byte l_t, long l_u) -> remappingFunction.apply(l_t, l_u));
    }

    long merge(byte key, long value, @NotNull LongLongBiFunction<? extends Long> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long merge(Byte key, @NotNull Long value, @NotNull BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
        Objects.requireNonNull(value);
        Objects.requireNonNull(remappingFunction);
        return this.merge(key != null ? key.byteValue() : (byte) 0, value.longValue(), (long l_t, long l_u) -> remappingFunction.apply(l_t, l_u));
    }

    long update(byte key, @NotNull LongUnaryOperator updateFunction);

    @Nullable
    default Long update(Byte key, @NotNull UnaryOperator<Long> updateFunction) {
        return this.update(key, (long l_t) -> updateFunction.apply(l_t));
    }

    long remapping(byte key, @NotNull ByteLongBiFunction<? extends Byte> remappingFunction, LongLongBiFunction<? extends Long> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long remapping(Byte key, @NotNull BiFunction<? super Byte, ? super Long, ? extends Byte> remappingFunction, @NotNull BiFunction<? super Long, ? super Long, ? extends Long> mergeFunction) {
        Objects.requireNonNull(remappingFunction);
        Objects.requireNonNull(mergeFunction);
        return this.remapping(key != null ? key.byteValue() : (byte) 0, (byte l_t, long l_u) -> remappingFunction.apply(l_t, l_u), (long l_t, long l_u) -> mergeFunction.apply(l_t, l_u));
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
        if (value != null && !(value instanceof Long)) return false;
        return this.containsValue(value != null ? ((Long) value).longValue() : (long) 0);
    }

    void forEach(@NotNull ByteLongBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Byte, ? super Long> action) {
        Objects.requireNonNull(action);
        this.forEach((byte l_t, long l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull ByteLongBiOperator function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Byte, ? super Long, ? extends Long> function) {
        Objects.requireNonNull(function);
        this.replaceAll((byte l_t, long l_u) -> function.apply(l_t, l_u));
    }

    long getDefaultReturnValueRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long getDefaultReturnValue() {
        return this.getDefaultReturnValueRaw();
    }

    long setDefaultReturnValue(long newDefault);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long setDefaultReturnValue(Long newDefault) {
        return this.setDefaultReturnValue(newDefault != null ? newDefault.longValue() : (long) 0);
    }

    @NotNull ByteSet keySet();

    @NotNull LongCollection values();

    @NotNull SetExt<Entry> rawEntrySet();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default @NotNull SetExt<Map.Entry<Byte, Long>> entrySet() {
        return (SetExt) this.rawEntrySet();
    }

    interface Entry extends Map.Entry<Byte, Long> {
        byte getRawKey();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         */
        @Deprecated
        default Byte getKey() {
            return this.getRawKey();
        }

        long getRawValue();

        long setValue(long newValue);

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         */
        @Deprecated
        default Long getValue() {
            return this.getRawValue();
        }

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         */
        @Deprecated
        default Long setValue(Long value) {
            return this.setValue(value != null ? value.longValue() : (long) 0);
        }
    }
}
