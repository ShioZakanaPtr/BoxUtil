package org.boxutil.base.api.container.objecttype;

import org.boxutil.base.api.container.MapExt;
import org.boxutil.base.api.container.SetExt;
import org.boxutil.base.api.container.bytetype.ByteCollection;
import org.boxutil.util.function.bytetype.ByteByteBiFunction;
import org.boxutil.util.function.bytetype.ByteUnaryOperator;
import org.boxutil.util.function.objecttype.ObjByteBiConsumer;
import org.boxutil.util.function.objecttype.ObjByteBiFunction;
import org.boxutil.util.function.objecttype.ObjByteBiOperator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public interface ObjByteMap<K> extends MapExt<K, Byte> {
    byte getRaw(Object key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Byte get(Object key) {
        return this.getRaw(key);
    }

    byte getOrDefault(Object key, byte defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Byte getOrDefault(Object key, Byte defaultValue) {
        return this.getOrDefault(key, defaultValue != null ? defaultValue.byteValue() : (byte) 0);
    }

    byte put(K key, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default @Nullable Byte put(K key, Byte value) {
        return this.put(key, value != null ? value.byteValue() : (byte) 0);
    }

    byte putIfAbsent(K key, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Byte putIfAbsent(K key, Byte value) {
        return this.putIfAbsent(key, value != null ? value.byteValue() : (byte) 0);
    }

    byte removeRaw(Object key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Byte remove(Object key) {
        return this.removeRaw(key);
    }

    boolean remove(Object key, byte value);

    default boolean remove(Object key, Object value) {
        if (value != null && !(value instanceof Byte)) return false;
        return this.remove(key, value != null ? ((Byte) value).byteValue() : (byte) 0);
    }

    byte replace(K key, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Byte replace(K key, Byte value) {
        return this.replace(key, value != null ? value.byteValue() : (byte) 0);
    }

    boolean replace(K key, byte oldValue, byte newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean replace(K key, Byte oldValue, Byte newValue) {
        return this.replace(key, oldValue != null ? oldValue.byteValue() : (byte) 0, newValue != null ? newValue.byteValue() : (byte) 0);
    }

    byte computeIfAbsentRaw(K key, @NotNull Function<? super K, ? extends Byte> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Byte computeIfAbsent(K key, @NotNull Function<? super K, ? extends Byte> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        return this.computeIfAbsentRaw(key, (K l_t) -> mappingFunction.apply(l_t));
    }

    byte computeIfPresent(K key, @NotNull ObjByteBiFunction<? super K, ? extends Byte> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Byte computeIfPresent(K key, @NotNull BiFunction<? super K, ? super Byte, ? extends Byte> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        return this.computeIfPresent(key, (K l_t, byte l_u) -> remappingFunction.apply(l_t, l_u));
    }

    byte compute(K key, @NotNull ObjByteBiFunction<? super K, ? extends Byte> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Byte compute(K key, @NotNull BiFunction<? super K, ? super Byte, ? extends Byte> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        return this.compute(key, (K l_t, byte l_u) -> remappingFunction.apply(l_t, l_u));
    }

    byte merge(K key, byte value, @NotNull ByteByteBiFunction<? extends Byte> remappingFunction);

    byte update(K key, @NotNull ByteUnaryOperator updateFunction);

    @Nullable
    default Byte update(K key, @NotNull UnaryOperator<Byte> updateFunction) {
        return this.update(key, (byte l_t) -> updateFunction.apply(l_t));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Byte merge(K key, @NotNull Byte value, @NotNull BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
        Objects.requireNonNull(value);
        Objects.requireNonNull(remappingFunction);
        return this.merge(key, value.byteValue(), (byte l_t, byte l_u) -> remappingFunction.apply(l_t, l_u));
    }

    byte remapping(K key, @NotNull ObjByteBiFunction<? super K, ? extends K> remappingFunction, ByteByteBiFunction<? extends Byte> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Byte remapping(K key, @NotNull BiFunction<? super K, ? super Byte, ? extends K> remappingFunction, @NotNull BiFunction<? super Byte, ? super Byte, ? extends Byte> mergeFunction) {
        Objects.requireNonNull(remappingFunction);
        Objects.requireNonNull(mergeFunction);
        return this.remapping(key, (K l_t, byte l_u) -> remappingFunction.apply(l_t, l_u), (byte l_t, byte l_u) -> mergeFunction.apply(l_t, l_u));
    }

    boolean containsValue(byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean containsValue(Object value) {
        if (value != null && !(value instanceof Byte)) return false;
        return this.containsValue(value != null ? ((Byte) value).byteValue() : (byte) 0);
    }

    void forEach(ObjByteBiConsumer<? super K> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void forEach(BiConsumer<? super K, ? super Byte> action) {
        Objects.requireNonNull(action);
        this.forEach((K l_t, byte l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(ObjByteBiOperator<? super K> function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void replaceAll(BiFunction<? super K, ? super Byte, ? extends Byte> function) {
        Objects.requireNonNull(function);
        this.replaceAll((K l_t, byte l_u) -> function.apply(l_t, l_u));
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

    @NotNull ByteCollection values();

    @NotNull SetExt<Entry<K>> rawEntrySet();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default @NotNull SetExt<Map.Entry<K, Byte>> entrySet() {
        return (SetExt) this.rawEntrySet();
    }

    interface Entry<K> extends Map.Entry<K, Byte> {
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
