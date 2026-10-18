package org.boxutil.base.api.container.bytetype;

import org.boxutil.base.api.container.MapExt;
import org.boxutil.base.api.container.SetExt;
import org.boxutil.base.api.container.shorttype.ShortCollection;
import org.boxutil.util.function.bytetype.ByteFunction;
import org.boxutil.util.function.bytetype.ByteShortBiConsumer;
import org.boxutil.util.function.bytetype.ByteShortBiFunction;
import org.boxutil.util.function.bytetype.ByteShortBiOperator;
import org.boxutil.util.function.shorttype.ShortShortBiFunction;
import org.boxutil.util.function.shorttype.ShortUnaryOperator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public interface ByteShortMap extends MapExt<Byte, Short> {
    short get(byte key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short get(Object key) {
        if (key != null && !(key instanceof Byte)) return this.getDefaultReturnValueRaw();
        return this.get(key != null ? ((Byte) key).byteValue() : (byte) 0);
    }

    short getOrDefault(byte key, short defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short getOrDefault(Object key, Short defaultValue) {
        if (key != null && !(key instanceof Byte)) return defaultValue;
        return this.getOrDefault(key != null ? ((Byte) key).byteValue() : (byte) 0, defaultValue != null ? defaultValue.shortValue() : (short) 0);
    }

    short put(byte key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default @Nullable Short put(Byte key, Short value) {
        return this.put(key != null ? key.byteValue() : (byte) 0, value != null ? value.shortValue() : (short) 0);
    }

    short putIfAbsent(byte key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short putIfAbsent(Byte key, Short value) {
        return this.putIfAbsent(key != null ? key.byteValue() : (byte) 0, value != null ? value.shortValue() : (short) 0);
    }

    short remove(byte key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short remove(Object key) {
        if (key != null && !(key instanceof Byte)) return this.getDefaultReturnValueRaw();
        return this.remove(key != null ? ((Byte) key).byteValue() : (byte) 0);
    }

    boolean remove(byte key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
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
     */
    @Deprecated
    default Short replace(Byte key, Short value) {
        return this.replace(key != null ? key.byteValue() : (byte) 0, value != null ? value.shortValue() : (short) 0);
    }

    boolean replace(byte key, short oldValue, short newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean replace(Byte key, Short oldValue, Short newValue) {
        return this.replace(key != null ? key.byteValue() : (byte) 0, oldValue != null ? oldValue.shortValue() : (short) 0, newValue != null ? newValue.shortValue() : (short) 0);
    }

    short computeIfAbsent(byte key, @NotNull ByteFunction<? extends Short> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short computeIfAbsent(Byte key, @NotNull Function<? super Byte, ? extends Short> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        return this.computeIfAbsent(key != null ? key.byteValue() : (byte) 0, (byte l_t) -> mappingFunction.apply(l_t));
    }

    short computeIfPresent(byte key, @NotNull ByteShortBiFunction<? extends Short> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short computeIfPresent(Byte key, @NotNull BiFunction<? super Byte, ? super Short, ? extends Short> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        return this.computeIfPresent(key != null ? key.byteValue() : (byte) 0, (byte l_t, short l_u) -> remappingFunction.apply(l_t, l_u));
    }

    short compute(byte key, @NotNull ByteShortBiFunction<? extends Short> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short compute(Byte key, @NotNull BiFunction<? super Byte, ? super Short, ? extends Short> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        return this.compute(key != null ? key.byteValue() : (byte) 0, (byte l_t, short l_u) -> remappingFunction.apply(l_t, l_u));
    }

    short merge(byte key, short value, @NotNull ShortShortBiFunction<? extends Short> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short merge(Byte key, @NotNull Short value, @NotNull BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
        Objects.requireNonNull(value);
        Objects.requireNonNull(remappingFunction);
        return this.merge(key != null ? key.byteValue() : (byte) 0, value.shortValue(), (short l_t, short l_u) -> remappingFunction.apply(l_t, l_u));
    }

    short update(byte key, @NotNull ShortUnaryOperator updateFunction);

    @Nullable
    default Short update(Byte key, @NotNull UnaryOperator<Short> updateFunction) {
        return this.update(key, (short l_t) -> updateFunction.apply(l_t));
    }

    short remapping(byte key, @NotNull ByteShortBiFunction<? extends Byte> remappingFunction, ShortShortBiFunction<? extends Short> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short remapping(Byte key, @NotNull BiFunction<? super Byte, ? super Short, ? extends Byte> remappingFunction, @NotNull BiFunction<? super Short, ? super Short, ? extends Short> mergeFunction) {
        Objects.requireNonNull(remappingFunction);
        Objects.requireNonNull(mergeFunction);
        return this.remapping(key != null ? key.byteValue() : (byte) 0, (byte l_t, short l_u) -> remappingFunction.apply(l_t, l_u), (short l_t, short l_u) -> mergeFunction.apply(l_t, l_u));
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
        if (value != null && !(value instanceof Short)) return false;
        return this.containsValue(value != null ? ((Short) value).shortValue() : (short) 0);
    }

    void forEach(@NotNull ByteShortBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Byte, ? super Short> action) {
        Objects.requireNonNull(action);
        this.forEach((byte l_t, short l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull ByteShortBiOperator function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Byte, ? super Short, ? extends Short> function) {
        Objects.requireNonNull(function);
        this.replaceAll((byte l_t, short l_u) -> function.apply(l_t, l_u));
    }

    short getDefaultReturnValueRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short getDefaultReturnValue() {
        return this.getDefaultReturnValueRaw();
    }

    short setDefaultReturnValue(short newDefault);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short setDefaultReturnValue(Short newDefault) {
        return this.setDefaultReturnValue(newDefault != null ? newDefault.shortValue() : (short) 0);
    }

    @NotNull ByteSet keySet();

    @NotNull ShortCollection values();

    @NotNull SetExt<Entry> rawEntrySet();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default @NotNull SetExt<Map.Entry<Byte, Short>> entrySet() {
        return (SetExt) this.rawEntrySet();
    }

    interface Entry extends Map.Entry<Byte, Short> {
        byte getRawKey();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         */
        @Deprecated
        default Byte getKey() {
            return this.getRawKey();
        }

        short getRawValue();

        short setValue(short newValue);

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         */
        @Deprecated
        default Short getValue() {
            return this.getRawValue();
        }

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         */
        @Deprecated
        default Short setValue(Short value) {
            return this.setValue(value != null ? value.shortValue() : (short) 0);
        }
    }
}
