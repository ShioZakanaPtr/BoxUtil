package org.boxutil.base.api.container.intt;

import org.boxutil.base.api.container.MapExt;
import org.boxutil.base.api.container.SetExt;
import org.boxutil.base.api.container.bytet.ByteCollection;
import org.boxutil.util.function.bytet.ByteByteBiFunction;
import org.boxutil.util.function.bytet.ByteUnaryOperator;
import org.boxutil.util.function.intt.IntByteBiConsumer;
import org.boxutil.util.function.intt.IntByteBiFunction;
import org.boxutil.util.function.intt.IntByteBiOperator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.function.*;

public interface IntByteMap extends MapExt<Integer, Byte> {
    byte get(int key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Byte get(Object key) {
        if (key != null && !(key instanceof Integer)) return this.getDefaultReturnValueRaw();
        return this.get(key != null ? ((Integer) key).intValue() : (int) 0);
    }

    byte getOrDefault(int key, byte defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Byte getOrDefault(Object key, Byte defaultValue) {
        if (key != null && !(key instanceof Integer)) return defaultValue;
        return this.getOrDefault(key != null ? ((Integer) key).intValue() : (int) 0, defaultValue != null ? defaultValue.byteValue() : (byte) 0);
    }

    byte put(int key, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default @Nullable Byte put(Integer key, Byte value) {
        return this.put(key != null ? key.intValue() : (int) 0, value != null ? value.byteValue() : (byte) 0);
    }

    byte putIfAbsent(int key, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Byte putIfAbsent(Integer key, Byte value) {
        return this.putIfAbsent(key != null ? key.intValue() : (int) 0, value != null ? value.byteValue() : (byte) 0);
    }

    byte remove(int key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Byte remove(Object key) {
        if (key != null && !(key instanceof Integer)) return this.getDefaultReturnValueRaw();
        return this.remove(key != null ? ((Integer) key).intValue() : (int) 0);
    }

    boolean remove(int key, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Integer)) return false;
        if (value != null && !(value instanceof Byte)) return false;
        return this.remove(key != null ? ((Integer) key).intValue() : (int) 0, value != null ? ((Byte) value).byteValue() : (byte) 0);
    }

    byte replace(int key, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Byte replace(Integer key, Byte value) {
        return this.replace(key != null ? key.intValue() : (int) 0, value != null ? value.byteValue() : (byte) 0);
    }

    boolean replace(int key, byte oldValue, byte newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean replace(Integer key, Byte oldValue, Byte newValue) {
        return this.replace(key != null ? key.intValue() : (int) 0, oldValue != null ? oldValue.byteValue() : (byte) 0, newValue != null ? newValue.byteValue() : (byte) 0);
    }

    byte computeIfAbsent(int key, @NotNull IntFunction<? extends Byte> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Byte computeIfAbsent(Integer key, @NotNull Function<? super Integer, ? extends Byte> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        return this.computeIfAbsent(key != null ? key.intValue() : (int) 0, (int l_t) -> mappingFunction.apply(l_t));
    }

    byte computeIfPresent(int key, @NotNull IntByteBiFunction<? extends Byte> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Byte computeIfPresent(Integer key, @NotNull BiFunction<? super Integer, ? super Byte, ? extends Byte> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        return this.computeIfPresent(key != null ? key.intValue() : (int) 0, (int l_t, byte l_u) -> remappingFunction.apply(l_t, l_u));
    }

    byte compute(int key, @NotNull IntByteBiFunction<? extends Byte> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Byte compute(Integer key, @NotNull BiFunction<? super Integer, ? super Byte, ? extends Byte> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        return this.compute(key != null ? key.intValue() : (int) 0, (int l_t, byte l_u) -> remappingFunction.apply(l_t, l_u));
    }

    byte merge(int key, byte value, @NotNull ByteByteBiFunction<? extends Byte> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Byte merge(Integer key, @NotNull Byte value, @NotNull BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
        Objects.requireNonNull(value);
        Objects.requireNonNull(remappingFunction);
        return this.merge(key != null ? key.intValue() : (int) 0, value.byteValue(), (byte l_t, byte l_u) -> remappingFunction.apply(l_t, l_u));
    }

    byte update(int key, @NotNull ByteUnaryOperator updateFunction);

    @Nullable
    default Byte update(Integer key, @NotNull UnaryOperator<Byte> updateFunction) {
        return this.update(key, (byte l_t) -> updateFunction.apply(l_t));
    }

    byte remapping(int key, @NotNull IntByteBiFunction<? extends Integer> remappingFunction, ByteByteBiFunction<? extends Byte> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Byte remapping(Integer key, @NotNull BiFunction<? super Integer, ? super Byte, ? extends Integer> remappingFunction, @NotNull BiFunction<? super Byte, ? super Byte, ? extends Byte> mergeFunction) {
        Objects.requireNonNull(remappingFunction);
        Objects.requireNonNull(mergeFunction);
        return this.remapping(key != null ? key.intValue() : (int) 0, (int l_t, byte l_u) -> remappingFunction.apply(l_t, l_u), (byte l_t, byte l_u) -> mergeFunction.apply(l_t, l_u));
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
        if (value != null && !(value instanceof Byte)) return false;
        return this.containsValue(value != null ? ((Byte) value).byteValue() : (byte) 0);
    }

    void forEach(@NotNull IntByteBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Integer, ? super Byte> action) {
        Objects.requireNonNull(action);
        this.forEach((int l_t, byte l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull IntByteBiOperator function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Integer, ? super Byte, ? extends Byte> function) {
        Objects.requireNonNull(function);
        this.replaceAll((int l_t, byte l_u) -> function.apply(l_t, l_u));
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

    @NotNull IntSet keySet();

    @NotNull ByteCollection values();

    @NotNull SetExt<Entry> rawEntrySet();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default @NotNull SetExt<Map.Entry<Integer, Byte>> entrySet() {
        return (SetExt) this.rawEntrySet();
    }

    interface Entry extends Map.Entry<Integer, Byte> {
        int getRawKey();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         */
        @Deprecated
        default Integer getKey() {
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
