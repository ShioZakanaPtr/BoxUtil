package io.github.shiozakanaptr.acext.api.collection.shorttype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.doubletype.DoubleCollection;
import io.github.shiozakanaptr.acext.util.function.shorttype.*;
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
public interface ShortDoubleMap extends MapExt<Short, Double> {
    double get(short key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #get(short)
     */
    @Deprecated
    default Double get(Object key) {
        if (key != null && !(key instanceof Short)) return this.getDefaultReturnValueRaw();
        return this.get(key != null ? ((Short) key).shortValue() : (short) 0);
    }

    double getOrDefault(short key, double defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(short, double)
     */
    @Deprecated
    default Double getOrDefault(Object key, Double defaultValue) {
        if (key != null && !(key instanceof Short)) return defaultValue;
        return this.getOrDefault(key != null ? ((Short) key).shortValue() : (short) 0, defaultValue != null ? defaultValue.doubleValue() : (double) 0);
    }

    double put(short key, double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(short, double)
     */
    @Deprecated
    default Double put(Short key, Double value) {
        return this.put(key != null ? key.shortValue() : (short) 0, value != null ? value.doubleValue() : (double) 0);
    }

    double putIfAbsent(short key, double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(short, double)
     */
    @Deprecated
    default Double putIfAbsent(Short key, Double value) {
        return this.putIfAbsent(key != null ? key.shortValue() : (short) 0, value != null ? value.doubleValue() : (double) 0);
    }

    double remove(short key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(short)
     */
    @Deprecated
    default Double remove(Object key) {
        if (key != null && !(key instanceof Short)) return this.getDefaultReturnValueRaw();
        return this.remove(key != null ? ((Short) key).shortValue() : (short) 0);
    }

    boolean remove(short key, double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(short, double)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Short)) return false;
        if (value != null && !(value instanceof Double)) return false;
        return this.remove(key != null ? ((Short) key).shortValue() : (short) 0, value != null ? ((Double) value).doubleValue() : (double) 0);
    }

    double replace(short key, double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(short, double)
     */
    @Deprecated
    default Double replace(Short key, Double value) {
        return this.replace(key != null ? key.shortValue() : (short) 0, value != null ? value.doubleValue() : (double) 0);
    }

    boolean replace(short key, double oldValue, double newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(short, double, double)
     */
    @Deprecated
    default boolean replace(Short key, Double oldValue, Double newValue) {
        return this.replace(key != null ? key.shortValue() : (short) 0, oldValue != null ? oldValue.doubleValue() : (double) 0, newValue != null ? newValue.doubleValue() : (double) 0);
    }

    double computeIfAbsent(short key, @NotNull ShortFunction<? extends Double> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(short, ShortFunction)
     */
    @Deprecated
    default Double computeIfAbsent(Short key, @NotNull Function<? super Short, ? extends Double> mappingFunction) {
        return this.computeIfAbsent(key != null ? key.shortValue() : (short) 0, (Short l_t) -> mappingFunction.apply(l_t));
    }

    double computeIfPresent(short key, @NotNull ShortDoubleBiFunction<? extends Double> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(short, ShortDoubleBiFunction)
     */
    @Deprecated
    default Double computeIfPresent(Short key, @NotNull BiFunction<? super Short, ? super Double, ? extends Double> remappingFunction) {
        return this.computeIfPresent(key != null ? key.shortValue() : (short) 0, (short l_t, double l_u) -> remappingFunction.apply(l_t, l_u));
    }

    double compute(short key, @NotNull ShortDoubleBiFunction<? extends Double> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(short, ShortDoubleBiFunction)
     */
    @Deprecated
    default Double compute(Short key, @NotNull BiFunction<? super Short, ? super Double, ? extends Double> remappingFunction) {
        return this.compute(key != null ? key.shortValue() : (short) 0, (short l_t, double l_u) -> remappingFunction.apply(l_t, l_u));
    }

    double merge(short key, double value, @NotNull DoubleDoubleBiFunction<? extends Double> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(short, double, DoubleDoubleBiFunction)
     */
    @Deprecated
    default Double merge(Short key, @NotNull Double value, @NotNull BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
        return this.merge(key != null ? key.shortValue() : (short) 0, value.doubleValue(), (double l_t, double l_u) -> remappingFunction.apply(l_t, l_u));
    }

    double update(short key, @NotNull DoubleUnaryOperator updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(short, DoubleUnaryOperator)
     */
    @Deprecated
    default Double update(Short key, @NotNull Function<? super Double, ? extends Double> updateFunction) {
        return this.update(key != null ? key.shortValue() : (short) 0, (double l_t) -> {
            final Double result;
            return (result = updateFunction.apply(l_t)) != null ? result : (double) 0;
        });
    }

    double remapping(short key, @NotNull ShortDoubleBiFunction<? extends Short> remappingFunction, DoubleDoubleBiFunction<? extends Double> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(short, ShortDoubleBiFunction, DoubleDoubleBiFunction)
     */
    @Deprecated
    default Double remapping(Short key, @NotNull BiFunction<? super Short, ? super Double, ? extends Short> remappingFunction, @NotNull BiFunction<? super Double, ? super Double, ? extends Double> mergeFunction) {
        return this.remapping(key != null ? key.shortValue() : (short) 0, (short l_t, double l_u) -> remappingFunction.apply(l_t, l_u), (double l_t, double l_u) -> mergeFunction.apply(l_t, l_u));
    }

    boolean swapAt(short from, short to);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #swapAt(short, short)
     */
    @Deprecated
    default boolean swapAt(Short from, Short to) {
        return this.swapAt(from != null ? from.shortValue() : (short) 0, to != null ? to.shortValue() : (short) 0);
    }

    boolean containsKey(short key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #containsKey(short)
     */
    @Deprecated
    default boolean containsKey(Object key) {
        if (key != null && !(key instanceof Short)) return false;
        return this.containsKey(key != null ? ((Short) key).shortValue() : (short) 0);
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

    void forEach(@NotNull ShortDoubleBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(ShortDoubleBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Short, ? super Double> action) {
        this.forEach((short l_t, double l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull ShortDoubleBiOperator function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(ShortDoubleBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Short, ? super Double, ? extends Double> function) {
        this.replaceAll((short l_t, double l_u) -> function.apply(l_t, l_u));
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
    ShortSet keySet();

    @NotNull
    DoubleCollection values();

    @NotNull
    EntrySetExt<ShortDoubleMap.Entry> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<MapExt.Entry<Short, Double>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface Entry extends MapExt.Entry<Short, Double> {
        short getKeyRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getKeyRaw()
         */
        @Deprecated
        default Short getKey() {
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
