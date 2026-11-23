package io.github.shiozakanaptr.acext.api.collection.doubletype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.shorttype.ShortCollection;
import io.github.shiozakanaptr.acext.util.function.doubletype.*;
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
public interface DoubleShortMap extends MapExt<Double, Short> {
    short get(double key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #get(double)
     */
    @Deprecated
    default Short get(Object key) {
        if (key != null && !(key instanceof Double)) return this.getDefaultReturnValueRaw();
        return this.get(key != null ? ((Double) key).doubleValue() : (double) 0);
    }

    short getOrDefault(double key, short defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(double, short)
     */
    @Deprecated
    default Short getOrDefault(Object key, Short defaultValue) {
        if (key != null && !(key instanceof Double)) return defaultValue;
        return this.getOrDefault(key != null ? ((Double) key).doubleValue() : (double) 0, defaultValue != null ? defaultValue.shortValue() : (short) 0);
    }

    short put(double key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(double, short)
     */
    @Deprecated
    default Short put(Double key, Short value) {
        return this.put(key != null ? key.doubleValue() : (double) 0, value != null ? value.shortValue() : (short) 0);
    }

    short putIfAbsent(double key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(double, short)
     */
    @Deprecated
    default Short putIfAbsent(Double key, Short value) {
        return this.putIfAbsent(key != null ? key.doubleValue() : (double) 0, value != null ? value.shortValue() : (short) 0);
    }

    short remove(double key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(double)
     */
    @Deprecated
    default Short remove(Object key) {
        if (key != null && !(key instanceof Double)) return this.getDefaultReturnValueRaw();
        return this.remove(key != null ? ((Double) key).doubleValue() : (double) 0);
    }

    boolean remove(double key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(double, short)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Double)) return false;
        if (value != null && !(value instanceof Short)) return false;
        return this.remove(key != null ? ((Double) key).doubleValue() : (double) 0, value != null ? ((Short) value).shortValue() : (short) 0);
    }

    short replace(double key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(double, short)
     */
    @Deprecated
    default Short replace(Double key, Short value) {
        return this.replace(key != null ? key.doubleValue() : (double) 0, value != null ? value.shortValue() : (short) 0);
    }

    boolean replace(double key, short oldValue, short newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(double, short, short)
     */
    @Deprecated
    default boolean replace(Double key, Short oldValue, Short newValue) {
        return this.replace(key != null ? key.doubleValue() : (double) 0, oldValue != null ? oldValue.shortValue() : (short) 0, newValue != null ? newValue.shortValue() : (short) 0);
    }

    short computeIfAbsent(double key, @NotNull DoubleFunction<? extends Short> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(double, DoubleFunction)
     */
    @Deprecated
    default Short computeIfAbsent(Double key, @NotNull Function<? super Double, ? extends Short> mappingFunction) {
        return this.computeIfAbsent(key != null ? key.doubleValue() : (double) 0, (Double l_t) -> mappingFunction.apply(l_t));
    }

    short computeIfPresent(double key, @NotNull DoubleShortBiFunction<? extends Short> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(double, DoubleShortBiFunction)
     */
    @Deprecated
    default Short computeIfPresent(Double key, @NotNull BiFunction<? super Double, ? super Short, ? extends Short> remappingFunction) {
        return this.computeIfPresent(key != null ? key.doubleValue() : (double) 0, (double l_t, short l_u) -> remappingFunction.apply(l_t, l_u));
    }

    short compute(double key, @NotNull DoubleShortBiFunction<? extends Short> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(double, DoubleShortBiFunction)
     */
    @Deprecated
    default Short compute(Double key, @NotNull BiFunction<? super Double, ? super Short, ? extends Short> remappingFunction) {
        return this.compute(key != null ? key.doubleValue() : (double) 0, (double l_t, short l_u) -> remappingFunction.apply(l_t, l_u));
    }

    short merge(double key, short value, @NotNull ShortShortBiFunction<? extends Short> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(double, short, ShortShortBiFunction)
     */
    @Deprecated
    default Short merge(Double key, @NotNull Short value, @NotNull BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
        return this.merge(key != null ? key.doubleValue() : (double) 0, value.shortValue(), (short l_t, short l_u) -> remappingFunction.apply(l_t, l_u));
    }

    short update(double key, @NotNull ShortUnaryOperator updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(double, ShortUnaryOperator)
     */
    @Deprecated
    default Short update(Double key, @NotNull Function<? super Short, ? extends Short> updateFunction) {
        return this.update(key != null ? key.doubleValue() : (double) 0, (short l_t) -> {
            final Short result;
            return (result = updateFunction.apply(l_t)) != null ? result : (short) 0;
        });
    }

    short remapping(double key, @NotNull DoubleShortBiFunction<? extends Double> remappingFunction, ShortShortBiFunction<? extends Short> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(double, DoubleShortBiFunction, ShortShortBiFunction)
     */
    @Deprecated
    default Short remapping(Double key, @NotNull BiFunction<? super Double, ? super Short, ? extends Double> remappingFunction, @NotNull BiFunction<? super Short, ? super Short, ? extends Short> mergeFunction) {
        return this.remapping(key != null ? key.doubleValue() : (double) 0, (double l_t, short l_u) -> remappingFunction.apply(l_t, l_u), (short l_t, short l_u) -> mergeFunction.apply(l_t, l_u));
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

    void forEach(@NotNull DoubleShortBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(DoubleShortBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Double, ? super Short> action) {
        this.forEach((double l_t, short l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull DoubleShortBiOperator function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(DoubleShortBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Double, ? super Short, ? extends Short> function) {
        this.replaceAll((double l_t, short l_u) -> function.apply(l_t, l_u));
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
    DoubleSet keySet();

    @NotNull
    ShortCollection values();

    @NotNull
    EntrySetExt<DoubleShortMap.Entry> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<MapExt.Entry<Double, Short>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface Entry extends MapExt.Entry<Double, Short> {
        double getKeyRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getKeyRaw()
         */
        @Deprecated
        default Double getKey() {
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
