package io.github.shiozakanaptr.acext.api.collection.doubletype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.bytetype.ByteCollection;
import io.github.shiozakanaptr.acext.util.function.doubletype.*;
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
public interface DoubleByteMap extends MapExt<Double, Byte> {
    byte get(double key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #get(double)
     */
    @Deprecated
    default Byte get(Object key) {
        if (key != null && !(key instanceof Double)) return this.getDefaultReturnValueRaw();
        return this.get(key != null ? ((Double) key).doubleValue() : (double) 0);
    }

    byte getOrDefault(double key, byte defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(double, byte)
     */
    @Deprecated
    default Byte getOrDefault(Object key, Byte defaultValue) {
        if (key != null && !(key instanceof Double)) return defaultValue;
        return this.getOrDefault(key != null ? ((Double) key).doubleValue() : (double) 0, defaultValue != null ? defaultValue.byteValue() : (byte) 0);
    }

    byte put(double key, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(double, byte)
     */
    @Deprecated
    default Byte put(Double key, Byte value) {
        return this.put(key != null ? key.doubleValue() : (double) 0, value != null ? value.byteValue() : (byte) 0);
    }

    byte putIfAbsent(double key, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(double, byte)
     */
    @Deprecated
    default Byte putIfAbsent(Double key, Byte value) {
        return this.putIfAbsent(key != null ? key.doubleValue() : (double) 0, value != null ? value.byteValue() : (byte) 0);
    }

    byte remove(double key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(double)
     */
    @Deprecated
    default Byte remove(Object key) {
        if (key != null && !(key instanceof Double)) return this.getDefaultReturnValueRaw();
        return this.remove(key != null ? ((Double) key).doubleValue() : (double) 0);
    }

    boolean remove(double key, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(double, byte)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Double)) return false;
        if (value != null && !(value instanceof Byte)) return false;
        return this.remove(key != null ? ((Double) key).doubleValue() : (double) 0, value != null ? ((Byte) value).byteValue() : (byte) 0);
    }

    byte replace(double key, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(double, byte)
     */
    @Deprecated
    default Byte replace(Double key, Byte value) {
        return this.replace(key != null ? key.doubleValue() : (double) 0, value != null ? value.byteValue() : (byte) 0);
    }

    boolean replace(double key, byte oldValue, byte newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(double, byte, byte)
     */
    @Deprecated
    default boolean replace(Double key, Byte oldValue, Byte newValue) {
        return this.replace(key != null ? key.doubleValue() : (double) 0, oldValue != null ? oldValue.byteValue() : (byte) 0, newValue != null ? newValue.byteValue() : (byte) 0);
    }

    byte computeIfAbsent(double key, @NotNull DoubleFunction<? extends Byte> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(double, DoubleFunction)
     */
    @Deprecated
    default Byte computeIfAbsent(Double key, @NotNull Function<? super Double, ? extends Byte> mappingFunction) {
        return this.computeIfAbsent(key != null ? key.doubleValue() : (double) 0, (Double l_t) -> mappingFunction.apply(l_t));
    }

    byte computeIfPresent(double key, @NotNull DoubleByteBiFunction<? extends Byte> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(double, DoubleByteBiFunction)
     */
    @Deprecated
    default Byte computeIfPresent(Double key, @NotNull BiFunction<? super Double, ? super Byte, ? extends Byte> remappingFunction) {
        return this.computeIfPresent(key != null ? key.doubleValue() : (double) 0, (double l_t, byte l_u) -> remappingFunction.apply(l_t, l_u));
    }

    byte compute(double key, @NotNull DoubleByteBiFunction<? extends Byte> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(double, DoubleByteBiFunction)
     */
    @Deprecated
    default Byte compute(Double key, @NotNull BiFunction<? super Double, ? super Byte, ? extends Byte> remappingFunction) {
        return this.compute(key != null ? key.doubleValue() : (double) 0, (double l_t, byte l_u) -> remappingFunction.apply(l_t, l_u));
    }

    byte merge(double key, byte value, @NotNull ByteByteBiFunction<? extends Byte> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(double, byte, ByteByteBiFunction)
     */
    @Deprecated
    default Byte merge(Double key, @NotNull Byte value, @NotNull BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
        return this.merge(key != null ? key.doubleValue() : (double) 0, value.byteValue(), (byte l_t, byte l_u) -> remappingFunction.apply(l_t, l_u));
    }

    byte update(double key, @NotNull ByteUnaryOperator updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(double, ByteUnaryOperator)
     */
    @Deprecated
    default Byte update(Double key, @NotNull Function<? super Byte, ? extends Byte> updateFunction) {
        return this.update(key != null ? key.doubleValue() : (double) 0, (byte l_t) -> {
            final Byte result;
            return (result = updateFunction.apply(l_t)) != null ? result : (byte) 0;
        });
    }

    byte remapping(double key, @NotNull DoubleByteBiFunction<? extends Double> remappingFunction, ByteByteBiFunction<? extends Byte> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(double, DoubleByteBiFunction, ByteByteBiFunction)
     */
    @Deprecated
    default Byte remapping(Double key, @NotNull BiFunction<? super Double, ? super Byte, ? extends Double> remappingFunction, @NotNull BiFunction<? super Byte, ? super Byte, ? extends Byte> mergeFunction) {
        return this.remapping(key != null ? key.doubleValue() : (double) 0, (double l_t, byte l_u) -> remappingFunction.apply(l_t, l_u), (byte l_t, byte l_u) -> mergeFunction.apply(l_t, l_u));
    }

    boolean swapAt(double from, double to);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #swapAt(double, double)
     */
    @Deprecated
    default boolean swapAt(Double from, Double to) {
        return this.swapAt(from != null ? from.doubleValue() : (double) 0, to != null ? to.doubleValue() : (double) 0);
    }

    boolean containsKey(double key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #containsKey(double)
     */
    @Deprecated
    default boolean containsKey(Object key) {
        if (key != null && !(key instanceof Double)) return false;
        return this.containsKey(key != null ? ((Double) key).doubleValue() : (double) 0);
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

    void forEach(@NotNull DoubleByteBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(DoubleByteBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Double, ? super Byte> action) {
        this.forEach((double l_t, byte l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull DoubleByteBiOperator function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(DoubleByteBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Double, ? super Byte, ? extends Byte> function) {
        this.replaceAll((double l_t, byte l_u) -> function.apply(l_t, l_u));
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
    DoubleSet keySet();

    @NotNull
    ByteCollection values();

    @NotNull
    EntrySetExt<DoubleByteMap.Entry> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<MapExt.Entry<Double, Byte>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface Entry extends MapExt.Entry<Double, Byte> {
        double getKeyRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getKeyRaw()
         */
        @Deprecated
        default Double getKey() {
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
