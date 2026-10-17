package org.boxutil.base.api.container.longt;

import org.boxutil.base.api.container.MapExt;
import org.boxutil.base.api.container.SetExt;
import org.boxutil.base.api.container.bytet.ByteCollection;
import org.boxutil.util.function.bytet.ByteByteBiFunction;
import org.boxutil.util.function.bytet.ByteUnaryOperator;
import org.boxutil.util.function.longt.LongByteBiConsumer;
import org.boxutil.util.function.longt.LongByteBiFunction;
import org.boxutil.util.function.longt.LongByteBiOperator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.function.*;

public interface LongByteMap extends MapExt<Long, Byte> {
    byte get(long key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Byte get(Object key) {
        if (key != null && !(key instanceof Long)) return this.getDefaultReturnValueRaw();
        return this.get(key != null ? ((Long) key).longValue() : (long) 0);
    }

    byte getOrDefault(long key, byte defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Byte getOrDefault(Object key, Byte defaultValue) {
        if (key != null && !(key instanceof Long)) return defaultValue;
        return this.getOrDefault(key != null ? ((Long) key).longValue() : (long) 0, defaultValue != null ? defaultValue.byteValue() : (byte) 0);
    }

    byte put(long key, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default @Nullable Byte put(Long key, Byte value) {
        return this.put(key != null ? key.longValue() : (long) 0, value != null ? value.byteValue() : (byte) 0);
    }

    byte putIfAbsent(long key, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Byte putIfAbsent(Long key, Byte value) {
        return this.putIfAbsent(key != null ? key.longValue() : (long) 0, value != null ? value.byteValue() : (byte) 0);
    }

    byte remove(long key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Byte remove(Object key) {
        if (key != null && !(key instanceof Long)) return this.getDefaultReturnValueRaw();
        return this.remove(key != null ? ((Long) key).longValue() : (long) 0);
    }

    boolean remove(long key, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Long)) return false;
        if (value != null && !(value instanceof Byte)) return false;
        return this.remove(key != null ? ((Long) key).longValue() : (long) 0, value != null ? ((Byte) value).byteValue() : (byte) 0);
    }

    byte replace(long key, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Byte replace(Long key, Byte value) {
        return this.replace(key != null ? key.longValue() : (long) 0, value != null ? value.byteValue() : (byte) 0);
    }

    boolean replace(long key, byte oldValue, byte newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean replace(Long key, Byte oldValue, Byte newValue) {
        return this.replace(key != null ? key.longValue() : (long) 0, oldValue != null ? oldValue.byteValue() : (byte) 0, newValue != null ? newValue.byteValue() : (byte) 0);
    }

    byte computeIfAbsent(long key, @NotNull LongFunction<? extends Byte> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Byte computeIfAbsent(Long key, @NotNull Function<? super Long, ? extends Byte> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        return this.computeIfAbsent(key != null ? key.longValue() : (long) 0, (long l_t) -> mappingFunction.apply(l_t));
    }

    byte computeIfPresent(long key, @NotNull LongByteBiFunction<? extends Byte> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Byte computeIfPresent(Long key, @NotNull BiFunction<? super Long, ? super Byte, ? extends Byte> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        return this.computeIfPresent(key != null ? key.longValue() : (long) 0, (long l_t, byte l_u) -> remappingFunction.apply(l_t, l_u));
    }

    byte compute(long key, @NotNull LongByteBiFunction<? extends Byte> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Byte compute(Long key, @NotNull BiFunction<? super Long, ? super Byte, ? extends Byte> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        return this.compute(key != null ? key.longValue() : (long) 0, (long l_t, byte l_u) -> remappingFunction.apply(l_t, l_u));
    }

    byte merge(long key, byte value, @NotNull ByteByteBiFunction<? extends Byte> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Byte merge(Long key, @NotNull Byte value, @NotNull BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
        Objects.requireNonNull(value);
        Objects.requireNonNull(remappingFunction);
        return this.merge(key != null ? key.longValue() : (long) 0, value.byteValue(), (byte l_t, byte l_u) -> remappingFunction.apply(l_t, l_u));
    }

    byte update(long key, @NotNull ByteUnaryOperator updateFunction);

    @Nullable
    default Byte update(Long key, @NotNull UnaryOperator<Byte> updateFunction) {
        return this.update(key, (byte l_t) -> updateFunction.apply(l_t));
    }

    byte remapping(long key, @NotNull LongByteBiFunction<? extends Long> remappingFunction, ByteByteBiFunction<? extends Byte> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Byte remapping(Long key, @NotNull BiFunction<? super Long, ? super Byte, ? extends Long> remappingFunction, @NotNull BiFunction<? super Byte, ? super Byte, ? extends Byte> mergeFunction) {
        Objects.requireNonNull(remappingFunction);
        Objects.requireNonNull(mergeFunction);
        return this.remapping(key != null ? key.longValue() : (long) 0, (long l_t, byte l_u) -> remappingFunction.apply(l_t, l_u), (byte l_t, byte l_u) -> mergeFunction.apply(l_t, l_u));
    }

    boolean containsKey(long key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean containsKey(Object key) {
        if (key != null && !(key instanceof Long)) return false;
        return this.containsKey(key != null ? ((Long) key).longValue() : (long) 0);
    }

    boolean containsValue(long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean containsValue(Object value) {
        if (value != null && !(value instanceof Byte)) return false;
        return this.containsValue(value != null ? ((Byte) value).byteValue() : (byte) 0);
    }

    void forEach(@NotNull LongByteBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Long, ? super Byte> action) {
        Objects.requireNonNull(action);
        this.forEach((long l_t, byte l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull LongByteBiOperator function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Long, ? super Byte, ? extends Byte> function) {
        Objects.requireNonNull(function);
        this.replaceAll((long l_t, byte l_u) -> function.apply(l_t, l_u));
    }

    byte getDefaultReturnValueRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Byte getDefaultReturnValue() {
        return this.getDefaultReturnValueRaw();
    }

    byte setDefaultReturnValue(byte newDefault);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Byte setDefaultReturnValue(Byte newDefault) {
        return this.setDefaultReturnValue(newDefault != null ? newDefault.byteValue() : (byte) 0);
    }

    @NotNull LongSet keySet();

    @NotNull ByteCollection values();

    @NotNull SetExt<Entry> rawEntrySet();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default @NotNull SetExt<Map.Entry<Long, Byte>> entrySet() {
        return (SetExt) this.rawEntrySet();
    }

    interface Entry extends Map.Entry<Long, Byte> {
        long getRawKey();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         */
        @Deprecated
        default Long getKey() {
            return this.getRawKey();
        }

        byte getRawValue();

        byte setValue(byte newValue);

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         */
        @Deprecated
        default Byte getValue() {
            return this.getRawValue();
        }

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         */
        @Deprecated
        default Byte setValue(Byte value) {
            return this.setValue(value != null ? value.byteValue() : (byte) 0);
        }
    }
}
