package io.github.shiozakanaptr.acext.api.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.inttype.IntCollection;
import io.github.shiozakanaptr.acext.util.function.bytetype.*;
import io.github.shiozakanaptr.acext.util.function.inttype.*;
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
public interface ByteIntMap extends MapExt<Byte, Integer> {
    int get(byte key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #get(byte)
     */
    @Deprecated
    default Integer get(Object key) {
        if (key != null && !(key instanceof Byte)) return this.getDefaultReturnValueRaw();
        return this.get(key != null ? ((Byte) key).byteValue() : (byte) 0);
    }

    int getOrDefault(byte key, int defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(byte, int)
     */
    @Deprecated
    default Integer getOrDefault(Object key, Integer defaultValue) {
        if (key != null && !(key instanceof Byte)) return defaultValue;
        return this.getOrDefault(key != null ? ((Byte) key).byteValue() : (byte) 0, defaultValue != null ? defaultValue.intValue() : (int) 0);
    }

    int put(byte key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(byte, int)
     */
    @Deprecated
    default Integer put(Byte key, Integer value) {
        return this.put(key != null ? key.byteValue() : (byte) 0, value != null ? value.intValue() : (int) 0);
    }

    int putIfAbsent(byte key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(byte, int)
     */
    @Deprecated
    default Integer putIfAbsent(Byte key, Integer value) {
        return this.putIfAbsent(key != null ? key.byteValue() : (byte) 0, value != null ? value.intValue() : (int) 0);
    }

    int remove(byte key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(byte)
     */
    @Deprecated
    default Integer remove(Object key) {
        if (key != null && !(key instanceof Byte)) return this.getDefaultReturnValueRaw();
        return this.remove(key != null ? ((Byte) key).byteValue() : (byte) 0);
    }

    boolean remove(byte key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(byte, int)
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
     * @see #replace(byte, int)
     */
    @Deprecated
    default Integer replace(Byte key, Integer value) {
        return this.replace(key != null ? key.byteValue() : (byte) 0, value != null ? value.intValue() : (int) 0);
    }

    boolean replace(byte key, int oldValue, int newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(byte, int, int)
     */
    @Deprecated
    default boolean replace(Byte key, Integer oldValue, Integer newValue) {
        return this.replace(key != null ? key.byteValue() : (byte) 0, oldValue != null ? oldValue.intValue() : (int) 0, newValue != null ? newValue.intValue() : (int) 0);
    }

    int computeIfAbsent(byte key, @NotNull ByteFunction<? extends Integer> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(byte, ByteFunction)
     */
    @Deprecated
    default Integer computeIfAbsent(Byte key, @NotNull Function<? super Byte, ? extends Integer> mappingFunction) {
        return this.computeIfAbsent(key != null ? key.byteValue() : (byte) 0, (Byte l_t) -> mappingFunction.apply(l_t));
    }

    int computeIfPresent(byte key, @NotNull ByteIntBiFunction<? extends Integer> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(byte, ByteIntBiFunction)
     */
    @Deprecated
    default Integer computeIfPresent(Byte key, @NotNull BiFunction<? super Byte, ? super Integer, ? extends Integer> remappingFunction) {
        return this.computeIfPresent(key != null ? key.byteValue() : (byte) 0, (byte l_t, int l_u) -> remappingFunction.apply(l_t, l_u));
    }

    int compute(byte key, @NotNull ByteIntBiFunction<? extends Integer> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(byte, ByteIntBiFunction)
     */
    @Deprecated
    default Integer compute(Byte key, @NotNull BiFunction<? super Byte, ? super Integer, ? extends Integer> remappingFunction) {
        return this.compute(key != null ? key.byteValue() : (byte) 0, (byte l_t, int l_u) -> remappingFunction.apply(l_t, l_u));
    }

    int merge(byte key, int value, @NotNull IntIntBiFunction<? extends Integer> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(byte, int, IntIntBiFunction)
     */
    @Deprecated
    default Integer merge(Byte key, @NotNull Integer value, @NotNull BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        return this.merge(key != null ? key.byteValue() : (byte) 0, value.intValue(), (int l_t, int l_u) -> remappingFunction.apply(l_t, l_u));
    }

    int update(byte key, @NotNull IntUnaryOperator updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(byte, IntUnaryOperator)
     */
    @Deprecated
    default Integer update(Byte key, @NotNull Function<? super Integer, ? extends Integer> updateFunction) {
        return this.update(key != null ? key.byteValue() : (byte) 0, (int l_t) -> {
            final Integer result;
            return (result = updateFunction.apply(l_t)) != null ? result : (int) 0;
        });
    }

    int remapping(byte key, @NotNull ByteIntBiFunction<? extends Byte> remappingFunction, IntIntBiFunction<? extends Integer> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(byte, ByteIntBiFunction, IntIntBiFunction)
     */
    @Deprecated
    default Integer remapping(Byte key, @NotNull BiFunction<? super Byte, ? super Integer, ? extends Byte> remappingFunction, @NotNull BiFunction<? super Integer, ? super Integer, ? extends Integer> mergeFunction) {
        return this.remapping(key != null ? key.byteValue() : (byte) 0, (byte l_t, int l_u) -> remappingFunction.apply(l_t, l_u), (int l_t, int l_u) -> mergeFunction.apply(l_t, l_u));
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

    boolean containsValue(int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #containsValue(int)
     */
    @Deprecated
    default boolean containsValue(Object value) {
        if (value != null && !(value instanceof Integer)) return false;
        return this.containsValue(value != null ? ((Integer) value).intValue() : (int) 0);
    }

    void forEach(@NotNull ByteIntBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(ByteIntBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Byte, ? super Integer> action) {
        this.forEach((byte l_t, int l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull ByteIntBiOperator function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(ByteIntBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Byte, ? super Integer, ? extends Integer> function) {
        this.replaceAll((byte l_t, int l_u) -> function.apply(l_t, l_u));
    }

    int getDefaultReturnValueRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getDefaultReturnValueRaw()
     */
    @Deprecated
    default Integer getDefaultReturnValue() {
        return this.getDefaultReturnValueRaw();
    }

    int setDefaultReturnValue(int newDefault);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #setDefaultReturnValue(int)
     */
    @Deprecated
    default Integer setDefaultReturnValue(Integer newDefault) {
        return this.setDefaultReturnValue(newDefault != null ? newDefault.intValue() : (int) 0);
    }

    @NotNull
    ByteSet keySet();

    @NotNull
    IntCollection values();

    @NotNull
    EntrySetExt<ByteIntMap.Entry> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<MapExt.Entry<Byte, Integer>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface Entry extends MapExt.Entry<Byte, Integer> {
        byte getKeyRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getKeyRaw()
         */
        @Deprecated
        default Byte getKey() {
            return this.getKeyRaw();
        }

        int getValueRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getValueRaw()
         */
        @Deprecated
        default Integer getValue() {
            return this.getValueRaw();
        }

        int setValue(int newValue);

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #setValue(int)
         */
        @Deprecated
        default Integer setValue(Integer value) {
            return this.setValue(value != null ? value.intValue() : (int) 0);
        }
    }

}
