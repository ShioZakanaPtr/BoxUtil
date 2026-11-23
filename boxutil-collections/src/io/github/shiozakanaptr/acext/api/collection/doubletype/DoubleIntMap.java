package io.github.shiozakanaptr.acext.api.collection.doubletype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.inttype.IntCollection;
import io.github.shiozakanaptr.acext.util.function.doubletype.*;
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
public interface DoubleIntMap extends MapExt<Double, Integer> {
    int get(double key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #get(double)
     */
    @Deprecated
    default Integer get(Object key) {
        if (key != null && !(key instanceof Double)) return this.getDefaultReturnValueRaw();
        return this.get(key != null ? ((Double) key).doubleValue() : (double) 0);
    }

    int getOrDefault(double key, int defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(double, int)
     */
    @Deprecated
    default Integer getOrDefault(Object key, Integer defaultValue) {
        if (key != null && !(key instanceof Double)) return defaultValue;
        return this.getOrDefault(key != null ? ((Double) key).doubleValue() : (double) 0, defaultValue != null ? defaultValue.intValue() : (int) 0);
    }

    int put(double key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(double, int)
     */
    @Deprecated
    default Integer put(Double key, Integer value) {
        return this.put(key != null ? key.doubleValue() : (double) 0, value != null ? value.intValue() : (int) 0);
    }

    int putIfAbsent(double key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(double, int)
     */
    @Deprecated
    default Integer putIfAbsent(Double key, Integer value) {
        return this.putIfAbsent(key != null ? key.doubleValue() : (double) 0, value != null ? value.intValue() : (int) 0);
    }

    int remove(double key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(double)
     */
    @Deprecated
    default Integer remove(Object key) {
        if (key != null && !(key instanceof Double)) return this.getDefaultReturnValueRaw();
        return this.remove(key != null ? ((Double) key).doubleValue() : (double) 0);
    }

    boolean remove(double key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(double, int)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Double)) return false;
        if (value != null && !(value instanceof Integer)) return false;
        return this.remove(key != null ? ((Double) key).doubleValue() : (double) 0, value != null ? ((Integer) value).intValue() : (int) 0);
    }

    int replace(double key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(double, int)
     */
    @Deprecated
    default Integer replace(Double key, Integer value) {
        return this.replace(key != null ? key.doubleValue() : (double) 0, value != null ? value.intValue() : (int) 0);
    }

    boolean replace(double key, int oldValue, int newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(double, int, int)
     */
    @Deprecated
    default boolean replace(Double key, Integer oldValue, Integer newValue) {
        return this.replace(key != null ? key.doubleValue() : (double) 0, oldValue != null ? oldValue.intValue() : (int) 0, newValue != null ? newValue.intValue() : (int) 0);
    }

    int computeIfAbsent(double key, @NotNull DoubleFunction<? extends Integer> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(double, DoubleFunction)
     */
    @Deprecated
    default Integer computeIfAbsent(Double key, @NotNull Function<? super Double, ? extends Integer> mappingFunction) {
        return this.computeIfAbsent(key != null ? key.doubleValue() : (double) 0, (Double l_t) -> mappingFunction.apply(l_t));
    }

    int computeIfPresent(double key, @NotNull DoubleIntBiFunction<? extends Integer> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(double, DoubleIntBiFunction)
     */
    @Deprecated
    default Integer computeIfPresent(Double key, @NotNull BiFunction<? super Double, ? super Integer, ? extends Integer> remappingFunction) {
        return this.computeIfPresent(key != null ? key.doubleValue() : (double) 0, (double l_t, int l_u) -> remappingFunction.apply(l_t, l_u));
    }

    int compute(double key, @NotNull DoubleIntBiFunction<? extends Integer> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(double, DoubleIntBiFunction)
     */
    @Deprecated
    default Integer compute(Double key, @NotNull BiFunction<? super Double, ? super Integer, ? extends Integer> remappingFunction) {
        return this.compute(key != null ? key.doubleValue() : (double) 0, (double l_t, int l_u) -> remappingFunction.apply(l_t, l_u));
    }

    int merge(double key, int value, @NotNull IntIntBiFunction<? extends Integer> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(double, int, IntIntBiFunction)
     */
    @Deprecated
    default Integer merge(Double key, @NotNull Integer value, @NotNull BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        return this.merge(key != null ? key.doubleValue() : (double) 0, value.intValue(), (int l_t, int l_u) -> remappingFunction.apply(l_t, l_u));
    }

    int update(double key, @NotNull IntUnaryOperator updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(double, IntUnaryOperator)
     */
    @Deprecated
    default Integer update(Double key, @NotNull Function<? super Integer, ? extends Integer> updateFunction) {
        return this.update(key != null ? key.doubleValue() : (double) 0, (int l_t) -> {
            final Integer result;
            return (result = updateFunction.apply(l_t)) != null ? result : (int) 0;
        });
    }

    int remapping(double key, @NotNull DoubleIntBiFunction<? extends Double> remappingFunction, IntIntBiFunction<? extends Integer> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(double, DoubleIntBiFunction, IntIntBiFunction)
     */
    @Deprecated
    default Integer remapping(Double key, @NotNull BiFunction<? super Double, ? super Integer, ? extends Double> remappingFunction, @NotNull BiFunction<? super Integer, ? super Integer, ? extends Integer> mergeFunction) {
        return this.remapping(key != null ? key.doubleValue() : (double) 0, (double l_t, int l_u) -> remappingFunction.apply(l_t, l_u), (int l_t, int l_u) -> mergeFunction.apply(l_t, l_u));
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

    void forEach(@NotNull DoubleIntBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(DoubleIntBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Double, ? super Integer> action) {
        this.forEach((double l_t, int l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull DoubleIntBiOperator function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(DoubleIntBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Double, ? super Integer, ? extends Integer> function) {
        this.replaceAll((double l_t, int l_u) -> function.apply(l_t, l_u));
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
    DoubleSet keySet();

    @NotNull
    IntCollection values();

    @NotNull
    EntrySetExt<DoubleIntMap.Entry> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<MapExt.Entry<Double, Integer>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface Entry extends MapExt.Entry<Double, Integer> {
        double getKeyRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getKeyRaw()
         */
        @Deprecated
        default Double getKey() {
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
