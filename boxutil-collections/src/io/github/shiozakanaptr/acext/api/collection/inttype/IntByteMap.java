package io.github.shiozakanaptr.acext.api.collection.inttype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.inttype.IntSet;
import io.github.shiozakanaptr.acext.api.collection.bytetype.ByteCollection;
import io.github.shiozakanaptr.acext.util.function.inttype.*;
import io.github.shiozakanaptr.acext.util.function.bytetype.*;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.function.BiConsumer;

public interface IntByteMap extends MapExt<Integer, Byte> {
    byte get(int key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #get(int)
     */
    @Deprecated
    default Byte get(Object key) {
        if (key != null && !(key instanceof Integer)) return this.getDefaultReturnValueRaw();
        return this.get(key != null ? ((Integer) key).intValue() : (int) 0);
    }

    byte getOrDefault(int key, byte defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(int, byte)
     */
    @Deprecated
    default Byte getOrDefault(Object key, Byte defaultValue) {
        if (key != null && !(key instanceof Integer)) return defaultValue;
        return this.getOrDefault(key != null ? ((Integer) key).intValue() : (int) 0, defaultValue != null ? defaultValue.byteValue() : (byte) 0);
    }

    byte put(int key, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(int, byte)
     */
    @Deprecated
    default Byte put(Integer key, Byte value) {
        return this.put(key != null ? key.intValue() : (int) 0, value != null ? value.byteValue() : (byte) 0);
    }

    byte putIfAbsent(int key, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(int, byte)
     */
    @Deprecated
    default Byte putIfAbsent(Integer key, Byte value) {
        return this.putIfAbsent(key != null ? key.intValue() : (int) 0, value != null ? value.byteValue() : (byte) 0);
    }

    byte remove(int key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(int)
     */
    @Deprecated
    default Byte remove(Object key) {
        if (key != null && !(key instanceof Integer)) return this.getDefaultReturnValueRaw();
        return this.remove(key != null ? ((Integer) key).intValue() : (int) 0);
    }

    boolean remove(int key, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(int, byte)
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
     * @see #replace(int, byte)
     */
    @Deprecated
    default Byte replace(Integer key, Byte value) {
        return this.replace(key != null ? key.intValue() : (int) 0, value != null ? value.byteValue() : (byte) 0);
    }

    boolean replace(int key, byte oldValue, byte newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(int, byte, byte)
     */
    @Deprecated
    default boolean replace(Integer key, Byte oldValue, Byte newValue) {
        return this.replace(key != null ? key.intValue() : (int) 0, oldValue != null ? oldValue.byteValue() : (byte) 0, newValue != null ? newValue.byteValue() : (byte) 0);
    }

    byte computeIfAbsent(int key, @NotNull IntFunction<? extends Byte> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(int, IntFunction)
     */
    @Deprecated
    default Byte computeIfAbsent(Integer key, @NotNull Function<? super Integer, ? extends Byte> mappingFunction) {
        return this.computeIfAbsent(key != null ? key.intValue() : (int) 0, (Integer l_t) -> mappingFunction.apply(l_t));
    }

    byte computeIfPresent(int key, @NotNull IntByteBiFunction<? extends Byte> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(int, IntByteBiFunction)
     */
    @Deprecated
    default Byte computeIfPresent(Integer key, @NotNull BiFunction<? super Integer, ? super Byte, ? extends Byte> remappingFunction) {
        return this.computeIfPresent(key != null ? key.intValue() : (int) 0, (int l_t, byte l_u) -> remappingFunction.apply(l_t, l_u));
    }

    byte compute(int key, @NotNull IntByteBiFunction<? extends Byte> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(int, IntByteBiFunction)
     */
    @Deprecated
    default Byte compute(Integer key, @NotNull BiFunction<? super Integer, ? super Byte, ? extends Byte> remappingFunction) {
        return this.compute(key != null ? key.intValue() : (int) 0, (int l_t, byte l_u) -> remappingFunction.apply(l_t, l_u));
    }

    byte merge(int key, byte value, @NotNull ByteByteBiFunction<? extends Byte> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(int, byte, ByteByteBiFunction)
     */
    @Deprecated
    default Byte merge(Integer key, @NotNull Byte value, @NotNull BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
        return this.merge(key != null ? key.intValue() : (int) 0, value != null ? value.byteValue() : (byte) 0, (byte l_t, byte l_u) -> remappingFunction.apply(l_t, l_u));
    }

    byte update(int key, @NotNull ByteUnaryOperator updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(int, ByteUnaryOperator)
     */
    @Deprecated
    default Byte update(Integer key, @NotNull Function<? super Byte, ? extends Byte> updateFunction) {
        return this.update(key != null ? key.intValue() : (int) 0, (byte l_t) -> {
            final Byte result;
            return (result = updateFunction.apply(l_t)) != null ? result : (byte) 0;
        });
    }

    byte remapping(int key, @NotNull IntByteBiFunction<? extends Integer> remappingFunction, ByteByteBiFunction<? extends Byte> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(int, IntByteBiFunction, ByteByteBiFunction)
     */
    @Deprecated
    default Byte remapping(Integer key, @NotNull BiFunction<? super Integer, ? super Byte, ? extends Integer> remappingFunction, @NotNull BiFunction<? super Byte, ? super Byte, ? extends Byte> mergeFunction) {
        return this.remapping(key != null ? key.intValue() : (int) 0, (int l_t, byte l_u) -> remappingFunction.apply(l_t, l_u), (byte l_t, byte l_u) -> mergeFunction.apply(l_t, l_u));
    }

    boolean swapAt(int from, int to);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #swapAt(int, int)
     */
    @Deprecated
    default boolean swapAt(Integer from, Integer to) {
        return this.swapAt(from != null ? from.intValue() : (int) 0, to != null ? to.intValue() : (int) 0);
    }

    boolean containsKey(int key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #containsKey(int)
     */
    @Deprecated
    default boolean containsKey(Object key) {
        if (key != null && !(key instanceof Integer)) return false;
        return this.containsKey(key != null ? ((Integer) key).intValue() : (int) 0);
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

    void forEach(@NotNull IntByteBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(IntByteBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Integer, ? super Byte> action) {
        this.forEach((int l_t, byte l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull IntByteBiOperator function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(IntByteBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Integer, ? super Byte, ? extends Byte> function) {
        this.replaceAll((int l_t, byte l_u) -> function.apply(l_t, l_u));
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
    IntSet keySet();

    @NotNull
    ByteCollection values();

    @NotNull
    EntrySetExt<IntByteMap.Entry> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<MapExt.Entry<Integer, Byte>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface Entry extends MapExt.Entry<Integer, Byte> {
        int getKeyRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getKeyRaw()
         */
        @Deprecated
        default Integer getKey() {
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
