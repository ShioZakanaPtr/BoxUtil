package io.github.shiozakanaptr.acext.api.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.doubletype.DoubleCollection;
import io.github.shiozakanaptr.acext.util.function.bytetype.*;
import io.github.shiozakanaptr.acext.util.function.doubletype.*;
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
public interface ByteDoubleMap extends MapExt<Byte, Double> {
    double get(byte key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #get(byte)
     */
    @Deprecated
    default Double get(Object key) {
        if (key != null && !(key instanceof Byte)) return this.getDefaultReturnValueRaw();
        return this.get(key != null ? ((Byte) key).byteValue() : (byte) 0);
    }

    double getOrDefault(byte key, double defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(byte, double)
     */
    @Deprecated
    default Double getOrDefault(Object key, Double defaultValue) {
        if (key != null && !(key instanceof Byte)) return defaultValue;
        return this.getOrDefault(key != null ? ((Byte) key).byteValue() : (byte) 0, defaultValue != null ? defaultValue.doubleValue() : (double) 0);
    }

    double put(byte key, double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(byte, double)
     */
    @Deprecated
    default Double put(Byte key, Double value) {
        return this.put(key != null ? key.byteValue() : (byte) 0, value != null ? value.doubleValue() : (double) 0);
    }

    double putIfAbsent(byte key, double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(byte, double)
     */
    @Deprecated
    default Double putIfAbsent(Byte key, Double value) {
        return this.putIfAbsent(key != null ? key.byteValue() : (byte) 0, value != null ? value.doubleValue() : (double) 0);
    }

    double remove(byte key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(byte)
     */
    @Deprecated
    default Double remove(Object key) {
        if (key != null && !(key instanceof Byte)) return this.getDefaultReturnValueRaw();
        return this.remove(key != null ? ((Byte) key).byteValue() : (byte) 0);
    }

    boolean remove(byte key, double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(byte, double)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Byte)) return false;
        if (value != null && !(value instanceof Double)) return false;
        return this.remove(key != null ? ((Byte) key).byteValue() : (byte) 0, value != null ? ((Double) value).doubleValue() : (double) 0);
    }

    double replace(byte key, double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(byte, double)
     */
    @Deprecated
    default Double replace(Byte key, Double value) {
        return this.replace(key != null ? key.byteValue() : (byte) 0, value != null ? value.doubleValue() : (double) 0);
    }

    boolean replace(byte key, double oldValue, double newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(byte, double, double)
     */
    @Deprecated
    default boolean replace(Byte key, Double oldValue, Double newValue) {
        return this.replace(key != null ? key.byteValue() : (byte) 0, oldValue != null ? oldValue.doubleValue() : (double) 0, newValue != null ? newValue.doubleValue() : (double) 0);
    }

    double computeIfAbsent(byte key, @NotNull ByteFunction<? extends Double> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(byte, ByteFunction)
     */
    @Deprecated
    default Double computeIfAbsent(Byte key, @NotNull Function<? super Byte, ? extends Double> mappingFunction) {
        return this.computeIfAbsent(key != null ? key.byteValue() : (byte) 0, (Byte l_t) -> mappingFunction.apply(l_t));
    }

    double computeIfPresent(byte key, @NotNull ByteDoubleBiFunction<? extends Double> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(byte, ByteDoubleBiFunction)
     */
    @Deprecated
    default Double computeIfPresent(Byte key, @NotNull BiFunction<? super Byte, ? super Double, ? extends Double> remappingFunction) {
        return this.computeIfPresent(key != null ? key.byteValue() : (byte) 0, (byte l_t, double l_u) -> remappingFunction.apply(l_t, l_u));
    }

    double compute(byte key, @NotNull ByteDoubleBiFunction<? extends Double> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(byte, ByteDoubleBiFunction)
     */
    @Deprecated
    default Double compute(Byte key, @NotNull BiFunction<? super Byte, ? super Double, ? extends Double> remappingFunction) {
        return this.compute(key != null ? key.byteValue() : (byte) 0, (byte l_t, double l_u) -> remappingFunction.apply(l_t, l_u));
    }

    double merge(byte key, double value, @NotNull DoubleDoubleBiFunction<? extends Double> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(byte, double, DoubleDoubleBiFunction)
     */
    @Deprecated
    default Double merge(Byte key, @NotNull Double value, @NotNull BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
        return this.merge(key != null ? key.byteValue() : (byte) 0, value != null ? value.doubleValue() : (double) 0, (double l_t, double l_u) -> remappingFunction.apply(l_t, l_u));
    }

    double update(byte key, @NotNull DoubleUnaryOperator updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(byte, DoubleUnaryOperator)
     */
    @Deprecated
    default Double update(Byte key, @NotNull Function<? super Double, ? extends Double> updateFunction) {
        return this.update(key != null ? key.byteValue() : (byte) 0, (double l_t) -> {
            final Double result;
            return (result = updateFunction.apply(l_t)) != null ? result : (double) 0;
        });
    }

    double remapping(byte key, @NotNull ByteDoubleBiFunction<? extends Byte> remappingFunction, DoubleDoubleBiFunction<? extends Double> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(byte, ByteDoubleBiFunction, DoubleDoubleBiFunction)
     */
    @Deprecated
    default Double remapping(Byte key, @NotNull BiFunction<? super Byte, ? super Double, ? extends Byte> remappingFunction, @NotNull BiFunction<? super Double, ? super Double, ? extends Double> mergeFunction) {
        return this.remapping(key != null ? key.byteValue() : (byte) 0, (byte l_t, double l_u) -> remappingFunction.apply(l_t, l_u), (double l_t, double l_u) -> mergeFunction.apply(l_t, l_u));
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

    boolean containsValue(double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #containsValue(double)
     */
    @Deprecated
    default boolean containsValue(Object value) {
        if (value != null && !(value instanceof Double)) return false;
        return this.containsValue(value != null ? ((Double) value).doubleValue() : (double) 0);
    }

    void forEach(@NotNull ByteDoubleBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(ByteDoubleBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Byte, ? super Double> action) {
        this.forEach((byte l_t, double l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull ByteDoubleBiOperator function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(ByteDoubleBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Byte, ? super Double, ? extends Double> function) {
        this.replaceAll((byte l_t, double l_u) -> function.apply(l_t, l_u));
    }

    double getDefaultReturnValueRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getDefaultReturnValueRaw()
     */
    @Deprecated
    default Double getDefaultReturnValue() {
        return this.getDefaultReturnValueRaw();
    }

    double setDefaultReturnValue(double newDefault);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #setDefaultReturnValue(double)
     */
    @Deprecated
    default Double setDefaultReturnValue(Double newDefault) {
        return this.setDefaultReturnValue(newDefault != null ? newDefault.doubleValue() : (double) 0);
    }

    @NotNull
    ByteSet keySet();

    @NotNull
    DoubleCollection values();

    @NotNull
    EntrySetExt<ByteDoubleMap.Entry> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<MapExt.Entry<Byte, Double>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface Entry extends MapExt.Entry<Byte, Double> {
        byte getKeyRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getKeyRaw()
         */
        @Deprecated
        default Byte getKey() {
            return this.getKeyRaw();
        }

        double getValueRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getValueRaw()
         */
        @Deprecated
        default Double getValue() {
            return this.getValueRaw();
        }

        double setValue(double newValue);

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #setValue(double)
         */
        @Deprecated
        default Double setValue(Double value) {
            return this.setValue(value != null ? value.doubleValue() : (double) 0);
        }
    }

}
