package io.github.shiozakanaptr.acext.api.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.shorttype.ShortCollection;
import io.github.shiozakanaptr.acext.util.function.bytetype.*;
import io.github.shiozakanaptr.acext.util.function.shorttype.*;
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
public interface ByteShortMap extends MapExt<Byte, Short> {
    short get(byte key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #get(byte)
     */
    @Deprecated
    default Short get(Object key) {
        if (key != null && !(key instanceof Byte)) return this.getDefaultReturnValueRaw();
        return this.get(key != null ? ((Byte) key).byteValue() : (byte) 0);
    }

    short getOrDefault(byte key, short defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(byte, short)
     */
    @Deprecated
    default Short getOrDefault(Object key, Short defaultValue) {
        if (key != null && !(key instanceof Byte)) return defaultValue;
        return this.getOrDefault(key != null ? ((Byte) key).byteValue() : (byte) 0, defaultValue != null ? defaultValue.shortValue() : (short) 0);
    }

    short put(byte key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(byte, short)
     */
    @Deprecated
    default Short put(Byte key, Short value) {
        return this.put(key != null ? key.byteValue() : (byte) 0, value != null ? value.shortValue() : (short) 0);
    }

    short putIfAbsent(byte key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(byte, short)
     */
    @Deprecated
    default Short putIfAbsent(Byte key, Short value) {
        return this.putIfAbsent(key != null ? key.byteValue() : (byte) 0, value != null ? value.shortValue() : (short) 0);
    }

    short remove(byte key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(byte)
     */
    @Deprecated
    default Short remove(Object key) {
        if (key != null && !(key instanceof Byte)) return this.getDefaultReturnValueRaw();
        return this.remove(key != null ? ((Byte) key).byteValue() : (byte) 0);
    }

    boolean remove(byte key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(byte, short)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Byte)) return false;
        if (value != null && !(value instanceof Short)) return false;
        return this.remove(key != null ? ((Byte) key).byteValue() : (byte) 0, value != null ? ((Short) value).shortValue() : (short) 0);
    }

    short replace(byte key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(byte, short)
     */
    @Deprecated
    default Short replace(Byte key, Short value) {
        return this.replace(key != null ? key.byteValue() : (byte) 0, value != null ? value.shortValue() : (short) 0);
    }

    boolean replace(byte key, short oldValue, short newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(byte, short, short)
     */
    @Deprecated
    default boolean replace(Byte key, Short oldValue, Short newValue) {
        return this.replace(key != null ? key.byteValue() : (byte) 0, oldValue != null ? oldValue.shortValue() : (short) 0, newValue != null ? newValue.shortValue() : (short) 0);
    }

    short computeIfAbsent(byte key, @NotNull ByteFunction<? extends Short> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(byte, ByteFunction)
     */
    @Deprecated
    default Short computeIfAbsent(Byte key, @NotNull Function<? super Byte, ? extends Short> mappingFunction) {
        return this.computeIfAbsent(key != null ? key.byteValue() : (byte) 0, (Byte l_t) -> mappingFunction.apply(l_t));
    }

    short computeIfPresent(byte key, @NotNull ByteShortBiFunction<? extends Short> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(byte, ByteShortBiFunction)
     */
    @Deprecated
    default Short computeIfPresent(Byte key, @NotNull BiFunction<? super Byte, ? super Short, ? extends Short> remappingFunction) {
        return this.computeIfPresent(key != null ? key.byteValue() : (byte) 0, (byte l_t, short l_u) -> remappingFunction.apply(l_t, l_u));
    }

    short compute(byte key, @NotNull ByteShortBiFunction<? extends Short> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(byte, ByteShortBiFunction)
     */
    @Deprecated
    default Short compute(Byte key, @NotNull BiFunction<? super Byte, ? super Short, ? extends Short> remappingFunction) {
        return this.compute(key != null ? key.byteValue() : (byte) 0, (byte l_t, short l_u) -> remappingFunction.apply(l_t, l_u));
    }

    short merge(byte key, short value, @NotNull ShortShortBiFunction<? extends Short> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(byte, short, ShortShortBiFunction)
     */
    @Deprecated
    default Short merge(Byte key, @NotNull Short value, @NotNull BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
        return this.merge(key != null ? key.byteValue() : (byte) 0, value.shortValue(), (short l_t, short l_u) -> remappingFunction.apply(l_t, l_u));
    }

    short update(byte key, @NotNull ShortUnaryOperator updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(byte, ShortUnaryOperator)
     */
    @Deprecated
    default Short update(Byte key, @NotNull Function<? super Short, ? extends Short> updateFunction) {
        return this.update(key != null ? key.byteValue() : (byte) 0, (short l_t) -> {
            final Short result;
            return (result = updateFunction.apply(l_t)) != null ? result : (short) 0;
        });
    }

    short remapping(byte key, @NotNull ByteShortBiFunction<? extends Byte> remappingFunction, ShortShortBiFunction<? extends Short> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(byte, ByteShortBiFunction, ShortShortBiFunction)
     */
    @Deprecated
    default Short remapping(Byte key, @NotNull BiFunction<? super Byte, ? super Short, ? extends Byte> remappingFunction, @NotNull BiFunction<? super Short, ? super Short, ? extends Short> mergeFunction) {
        return this.remapping(key != null ? key.byteValue() : (byte) 0, (byte l_t, short l_u) -> remappingFunction.apply(l_t, l_u), (short l_t, short l_u) -> mergeFunction.apply(l_t, l_u));
    }

    boolean swapAt(byte from, byte to);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #swapAt(byte, byte)
     */
    @Deprecated
    default boolean swapAt(Byte from, Byte to) {
        return this.swapAt(from != null ? from.byteValue() : (byte) 0, to != null ? to.byteValue() : (byte) 0);
    }

    boolean containsKey(byte key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #containsKey(byte)
     */
    @Deprecated
    default boolean containsKey(Object key) {
        if (key != null && !(key instanceof Byte)) return false;
        return this.containsKey(key != null ? ((Byte) key).byteValue() : (byte) 0);
    }

    boolean containsValue(short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #containsValue(short)
     */
    @Deprecated
    default boolean containsValue(Object value) {
        if (value != null && !(value instanceof Short)) return false;
        return this.containsValue(value != null ? ((Short) value).shortValue() : (short) 0);
    }

    void forEach(@NotNull ByteShortBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(ByteShortBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Byte, ? super Short> action) {
        this.forEach((byte l_t, short l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull ByteShortBiOperator function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(ByteShortBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Byte, ? super Short, ? extends Short> function) {
        this.replaceAll((byte l_t, short l_u) -> function.apply(l_t, l_u));
    }

    short getDefaultReturnValueRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getDefaultReturnValueRaw()
     */
    @Deprecated
    default Short getDefaultReturnValue() {
        return this.getDefaultReturnValueRaw();
    }

    short setDefaultReturnValue(short newDefault);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #setDefaultReturnValue(short)
     */
    @Deprecated
    default Short setDefaultReturnValue(Short newDefault) {
        return this.setDefaultReturnValue(newDefault != null ? newDefault.shortValue() : (short) 0);
    }

    @NotNull
    ByteSet keySet();

    @NotNull
    ShortCollection values();

    @NotNull
    EntrySetExt<ByteShortMap.Entry> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<MapExt.Entry<Byte, Short>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface Entry extends MapExt.Entry<Byte, Short> {
        byte getKeyRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getKeyRaw()
         */
        @Deprecated
        default Byte getKey() {
            return this.getKeyRaw();
        }

        short getValueRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getValueRaw()
         */
        @Deprecated
        default Short getValue() {
            return this.getValueRaw();
        }

        short setValue(short newValue);

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #setValue(short)
         */
        @Deprecated
        default Short setValue(Short value) {
            return this.setValue(value != null ? value.shortValue() : (short) 0);
        }
    }

}
