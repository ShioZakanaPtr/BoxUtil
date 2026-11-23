package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.bytetype.ByteCollection;
import io.github.shiozakanaptr.acext.util.function.objecttype.*;
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
public interface ObjByteMap<K> extends MapExt<K, Byte> {
    byte getRaw(Object key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getRaw(Object)
     */
    @Deprecated
    default Byte get(Object key) {
        return this.getRaw(key);
    }

    byte getOrDefault(Object key, byte defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(Object, byte)
     */
    @Deprecated
    default Byte getOrDefault(Object key, Byte defaultValue) {
        return this.getOrDefault(key, defaultValue != null ? defaultValue.byteValue() : (byte) 0);
    }

    byte put(K key, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(K, byte)
     */
    @Deprecated
    default Byte put(K key, Byte value) {
        return this.put(key, value != null ? value.byteValue() : (byte) 0);
    }

    byte putIfAbsent(K key, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(K, byte)
     */
    @Deprecated
    default Byte putIfAbsent(K key, Byte value) {
        return this.putIfAbsent(key, value != null ? value.byteValue() : (byte) 0);
    }

    byte removeRaw(Object key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeRaw(Object)
     */
    @Deprecated
    default Byte remove(Object key) {
        return this.removeRaw(key);
    }

    boolean remove(Object key, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(Object, byte)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (value != null && !(value instanceof Byte)) return false;
        return this.remove(key, value != null ? ((Byte) value).byteValue() : (byte) 0);
    }

    byte replace(K key, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(K, byte)
     */
    @Deprecated
    default Byte replace(K key, Byte value) {
        return this.replace(key, value != null ? value.byteValue() : (byte) 0);
    }

    boolean replace(K key, byte oldValue, byte newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(K, byte, byte)
     */
    @Deprecated
    default boolean replace(K key, Byte oldValue, Byte newValue) {
        return this.replace(key, oldValue != null ? oldValue.byteValue() : (byte) 0, newValue != null ? newValue.byteValue() : (byte) 0);
    }

    byte computeIfAbsentRaw(K key, @NotNull Function<? super K, ? extends Byte> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsentRaw(K, Function)
     */
    @Deprecated
    default Byte computeIfAbsent(K key, @NotNull Function<? super K, ? extends Byte> mappingFunction) {
        return this.computeIfAbsentRaw(key, (K l_t) -> mappingFunction.apply(l_t));
    }

    byte computeIfPresent(K key, @NotNull ObjByteBiFunction<? super K, ? extends Byte> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(K, ObjByteBiFunction)
     */
    @Deprecated
    default Byte computeIfPresent(K key, @NotNull BiFunction<? super K, ? super Byte, ? extends Byte> remappingFunction) {
        return this.computeIfPresent(key, (K l_t, byte l_u) -> remappingFunction.apply(l_t, l_u));
    }

    byte compute(K key, @NotNull ObjByteBiFunction<? super K, ? extends Byte> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(K, ObjByteBiFunction)
     */
    @Deprecated
    default Byte compute(K key, @NotNull BiFunction<? super K, ? super Byte, ? extends Byte> remappingFunction) {
        return this.compute(key, (K l_t, byte l_u) -> remappingFunction.apply(l_t, l_u));
    }

    byte merge(K key, byte value, @NotNull ByteByteBiFunction<? extends Byte> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(K, byte, ByteByteBiFunction)
     */
    @Deprecated
    default Byte merge(K key, @NotNull Byte value, @NotNull BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
        return this.merge(key, value != null ? value.byteValue() : (byte) 0, (byte l_t, byte l_u) -> remappingFunction.apply(l_t, l_u));
    }

    byte update(K key, @NotNull ByteUnaryOperator updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(K, ByteUnaryOperator)
     */
    @Deprecated
    default Byte update(K key, @NotNull Function<? super Byte, ? extends Byte> updateFunction) {
        return this.update(key, (byte l_t) -> {
            final Byte result;
            return (result = updateFunction.apply(l_t)) != null ? result : (byte) 0;
        });
    }

    byte remapping(K key, @NotNull ObjByteBiFunction<? super K, ? extends K> remappingFunction, ByteByteBiFunction<? extends Byte> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(K, ObjByteBiFunction, ByteByteBiFunction)
     */
    @Deprecated
    default Byte remapping(K key, @NotNull BiFunction<? super K, ? super Byte, ? extends K> remappingFunction, @NotNull BiFunction<? super Byte, ? super Byte, ? extends Byte> mergeFunction) {
        return this.remapping(key, (K l_t, byte l_u) -> remappingFunction.apply(l_t, l_u), (byte l_t, byte l_u) -> mergeFunction.apply(l_t, l_u));
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

    void forEach(@NotNull ObjByteBiConsumer<? super K> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(ObjByteBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super K, ? super Byte> action) {
        this.forEach((K l_t, byte l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull ObjByteBiOperator<? super K> function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(ObjByteBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super K, ? super Byte, ? extends Byte> function) {
        this.replaceAll((K l_t, byte l_u) -> function.apply(l_t, l_u));
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
    ObjSet<K> keySet();

    @NotNull
    ByteCollection values();

    @NotNull
    EntrySetExt<ObjByteMap.Entry<K>> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<MapExt.Entry<K, Byte>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface Entry<K> extends MapExt.Entry<K, Byte> {

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
