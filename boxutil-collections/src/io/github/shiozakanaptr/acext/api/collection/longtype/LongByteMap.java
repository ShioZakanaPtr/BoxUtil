package io.github.shiozakanaptr.acext.api.collection.longtype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.bytetype.ByteCollection;
import io.github.shiozakanaptr.acext.util.function.longtype.*;
import io.github.shiozakanaptr.acext.util.function.bytetype.*;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.function.BiConsumer;

/**
 * Type specialization interface.
 *
 * @see MapExt
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface LongByteMap extends MapExt<Long, Byte> {
    byte get(long key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #get(long)
     */
    @Deprecated
    default Byte get(Object key) {
        if (key != null && !(key instanceof Long)) return this.getDefaultReturnValueRaw();
        return this.get(key != null ? ((Long) key).longValue() : (long) 0);
    }

    byte getOrDefault(long key, byte defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(long, byte)
     */
    @Deprecated
    default Byte getOrDefault(Object key, Byte defaultValue) {
        if (key != null && !(key instanceof Long)) return defaultValue;
        return this.getOrDefault(key != null ? ((Long) key).longValue() : (long) 0, defaultValue != null ? defaultValue.byteValue() : (byte) 0);
    }

    byte put(long key, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(long, byte)
     */
    @Deprecated
    default Byte put(Long key, Byte value) {
        return this.put(key != null ? key.longValue() : (long) 0, value != null ? value.byteValue() : (byte) 0);
    }

    byte putIfAbsent(long key, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(long, byte)
     */
    @Deprecated
    default Byte putIfAbsent(Long key, Byte value) {
        return this.putIfAbsent(key != null ? key.longValue() : (long) 0, value != null ? value.byteValue() : (byte) 0);
    }

    byte remove(long key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(long)
     */
    @Deprecated
    default Byte remove(Object key) {
        if (key != null && !(key instanceof Long)) return this.getDefaultReturnValueRaw();
        return this.remove(key != null ? ((Long) key).longValue() : (long) 0);
    }

    boolean remove(long key, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(long, byte)
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
     * @see #replace(long, byte)
     */
    @Deprecated
    default Byte replace(Long key, Byte value) {
        return this.replace(key != null ? key.longValue() : (long) 0, value != null ? value.byteValue() : (byte) 0);
    }

    boolean replace(long key, byte oldValue, byte newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(long, byte, byte)
     */
    @Deprecated
    default boolean replace(Long key, Byte oldValue, Byte newValue) {
        return this.replace(key != null ? key.longValue() : (long) 0, oldValue != null ? oldValue.byteValue() : (byte) 0, newValue != null ? newValue.byteValue() : (byte) 0);
    }

    byte computeIfAbsent(long key, @NotNull LongFunction<? extends Byte> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(long, LongFunction)
     */
    @Deprecated
    default Byte computeIfAbsent(Long key, @NotNull Function<? super Long, ? extends Byte> mappingFunction) {
        return this.computeIfAbsent(key != null ? key.longValue() : (long) 0, (Long l_t) -> mappingFunction.apply(l_t));
    }

    byte computeIfPresent(long key, @NotNull LongByteBiFunction<? extends Byte> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(long, LongByteBiFunction)
     */
    @Deprecated
    default Byte computeIfPresent(Long key, @NotNull BiFunction<? super Long, ? super Byte, ? extends Byte> remappingFunction) {
        return this.computeIfPresent(key != null ? key.longValue() : (long) 0, (long l_t, byte l_u) -> remappingFunction.apply(l_t, l_u));
    }

    byte compute(long key, @NotNull LongByteBiFunction<? extends Byte> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(long, LongByteBiFunction)
     */
    @Deprecated
    default Byte compute(Long key, @NotNull BiFunction<? super Long, ? super Byte, ? extends Byte> remappingFunction) {
        return this.compute(key != null ? key.longValue() : (long) 0, (long l_t, byte l_u) -> remappingFunction.apply(l_t, l_u));
    }

    byte merge(long key, byte value, @NotNull ByteByteBiFunction<? extends Byte> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(long, byte, ByteByteBiFunction)
     */
    @Deprecated
    default Byte merge(Long key, @NotNull Byte value, @NotNull BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
        return this.merge(key != null ? key.longValue() : (long) 0, value.byteValue(), (byte l_t, byte l_u) -> remappingFunction.apply(l_t, l_u));
    }

    byte update(long key, @NotNull ByteUnaryOperator updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(long, ByteUnaryOperator)
     */
    @Deprecated
    default Byte update(Long key, @NotNull Function<? super Byte, ? extends Byte> updateFunction) {
        return this.update(key != null ? key.longValue() : (long) 0, (byte l_t) -> {
            final Byte result;
            return (result = updateFunction.apply(l_t)) != null ? result : (byte) 0;
        });
    }

    byte remapping(long key, @NotNull LongByteBiFunction<? extends Long> remappingFunction, ByteByteBiFunction<? extends Byte> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(long, LongByteBiFunction, ByteByteBiFunction)
     */
    @Deprecated
    default Byte remapping(Long key, @NotNull BiFunction<? super Long, ? super Byte, ? extends Long> remappingFunction, @NotNull BiFunction<? super Byte, ? super Byte, ? extends Byte> mergeFunction) {
        return this.remapping(key != null ? key.longValue() : (long) 0, (long l_t, byte l_u) -> remappingFunction.apply(l_t, l_u), (byte l_t, byte l_u) -> mergeFunction.apply(l_t, l_u));
    }

    boolean swapAt(long from, long to);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #swapAt(long, long)
     */
    @Deprecated
    default boolean swapAt(Long from, Long to) {
        return this.swapAt(from != null ? from.longValue() : (long) 0, to != null ? to.longValue() : (long) 0);
    }

    boolean containsKey(long key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #containsKey(long)
     */
    @Deprecated
    default boolean containsKey(Object key) {
        if (key != null && !(key instanceof Long)) return false;
        return this.containsKey(key != null ? ((Long) key).longValue() : (long) 0);
    }

    boolean containsValue(byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #containsValue(byte)
     */
    @Deprecated
    default boolean containsValue(Object value) {
        if (value != null && !(value instanceof Byte)) return false;
        return this.containsValue(value != null ? ((Byte) value).byteValue() : (byte) 0);
    }

    void forEach(@NotNull LongByteBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(LongByteBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Long, ? super Byte> action) {
        this.forEach((long l_t, byte l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull LongByteBiOperator function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(LongByteBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Long, ? super Byte, ? extends Byte> function) {
        this.replaceAll((long l_t, byte l_u) -> function.apply(l_t, l_u));
    }

    byte getDefaultReturnValueRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getDefaultReturnValueRaw()
     */
    @Deprecated
    default Byte getDefaultReturnValue() {
        return this.getDefaultReturnValueRaw();
    }

    byte setDefaultReturnValue(byte newDefault);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #setDefaultReturnValue(byte)
     */
    @Deprecated
    default Byte setDefaultReturnValue(Byte newDefault) {
        return this.setDefaultReturnValue(newDefault != null ? newDefault.byteValue() : (byte) 0);
    }

    @NotNull
    LongSet keySet();

    @NotNull
    ByteCollection values();

    @NotNull
    EntrySetExt<LongByteMap.Entry> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<MapExt.Entry<Long, Byte>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface Entry extends MapExt.Entry<Long, Byte> {
        long getKeyRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getKeyRaw()
         */
        @Deprecated
        default Long getKey() {
            return this.getKeyRaw();
        }

        byte getValueRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getValueRaw()
         */
        @Deprecated
        default Byte getValue() {
            return this.getValueRaw();
        }

        byte setValue(byte newValue);

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #setValue(byte)
         */
        @Deprecated
        default Byte setValue(Byte value) {
            return this.setValue(value != null ? value.byteValue() : (byte) 0);
        }
    }

}
