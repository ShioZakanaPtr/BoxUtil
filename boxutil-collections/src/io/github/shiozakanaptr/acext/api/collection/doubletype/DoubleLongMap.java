package io.github.shiozakanaptr.acext.api.collection.doubletype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.doubletype.DoubleSet;
import io.github.shiozakanaptr.acext.api.collection.longtype.LongCollection;
import io.github.shiozakanaptr.acext.util.function.doubletype.*;
import io.github.shiozakanaptr.acext.util.function.longtype.*;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.function.BiConsumer;

public interface DoubleLongMap extends MapExt<Double, Long> {
    long get(double key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #get(double)
     */
    @Deprecated
    default Long get(Object key) {
        if (key != null && !(key instanceof Double)) return this.getDefaultReturnValueRaw();
        return this.get(key != null ? ((Double) key).doubleValue() : (double) 0);
    }

    long getOrDefault(double key, long defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(double, long)
     */
    @Deprecated
    default Long getOrDefault(Object key, Long defaultValue) {
        if (key != null && !(key instanceof Double)) return defaultValue;
        return this.getOrDefault(key != null ? ((Double) key).doubleValue() : (double) 0, defaultValue != null ? defaultValue.longValue() : (long) 0);
    }

    long put(double key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(double, long)
     */
    @Deprecated
    default Long put(Double key, Long value) {
        return this.put(key != null ? key.doubleValue() : (double) 0, value != null ? value.longValue() : (long) 0);
    }

    long putIfAbsent(double key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(double, long)
     */
    @Deprecated
    default Long putIfAbsent(Double key, Long value) {
        return this.putIfAbsent(key != null ? key.doubleValue() : (double) 0, value != null ? value.longValue() : (long) 0);
    }

    long remove(double key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(double)
     */
    @Deprecated
    default Long remove(Object key) {
        if (key != null && !(key instanceof Double)) return this.getDefaultReturnValueRaw();
        return this.remove(key != null ? ((Double) key).doubleValue() : (double) 0);
    }

    boolean remove(double key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(double, long)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Double)) return false;
        if (value != null && !(value instanceof Long)) return false;
        return this.remove(key != null ? ((Double) key).doubleValue() : (double) 0, value != null ? ((Long) value).longValue() : (long) 0);
    }

    long replace(double key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(double, long)
     */
    @Deprecated
    default Long replace(Double key, Long value) {
        return this.replace(key != null ? key.doubleValue() : (double) 0, value != null ? value.longValue() : (long) 0);
    }

    boolean replace(double key, long oldValue, long newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(double, long, long)
     */
    @Deprecated
    default boolean replace(Double key, Long oldValue, Long newValue) {
        return this.replace(key != null ? key.doubleValue() : (double) 0, oldValue != null ? oldValue.longValue() : (long) 0, newValue != null ? newValue.longValue() : (long) 0);
    }

    long computeIfAbsent(double key, @NotNull DoubleFunction<? extends Long> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(double, DoubleFunction)
     */
    @Deprecated
    default Long computeIfAbsent(Double key, @NotNull Function<? super Double, ? extends Long> mappingFunction) {
        return this.computeIfAbsent(key != null ? key.doubleValue() : (double) 0, (Double l_t) -> mappingFunction.apply(l_t));
    }

    long computeIfPresent(double key, @NotNull DoubleLongBiFunction<? extends Long> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(double, DoubleLongBiFunction)
     */
    @Deprecated
    default Long computeIfPresent(Double key, @NotNull BiFunction<? super Double, ? super Long, ? extends Long> remappingFunction) {
        return this.computeIfPresent(key != null ? key.doubleValue() : (double) 0, (double l_t, long l_u) -> remappingFunction.apply(l_t, l_u));
    }

    long compute(double key, @NotNull DoubleLongBiFunction<? extends Long> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(double, DoubleLongBiFunction)
     */
    @Deprecated
    default Long compute(Double key, @NotNull BiFunction<? super Double, ? super Long, ? extends Long> remappingFunction) {
        return this.compute(key != null ? key.doubleValue() : (double) 0, (double l_t, long l_u) -> remappingFunction.apply(l_t, l_u));
    }

    long merge(double key, long value, @NotNull LongLongBiFunction<? extends Long> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(double, long, LongLongBiFunction)
     */
    @Deprecated
    default Long merge(Double key, @NotNull Long value, @NotNull BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
        return this.merge(key != null ? key.doubleValue() : (double) 0, value != null ? value.longValue() : (long) 0, (long l_t, long l_u) -> remappingFunction.apply(l_t, l_u));
    }

    long update(double key, @NotNull LongUnaryOperator updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(double, LongUnaryOperator)
     */
    @Deprecated
    default Long update(Double key, @NotNull Function<? super Long, ? extends Long> updateFunction) {
        return this.update(key != null ? key.doubleValue() : (double) 0, (long l_t) -> {
            final Long result;
            return (result = updateFunction.apply(l_t)) != null ? result : (long) 0;
        });
    }

    long remapping(double key, @NotNull DoubleLongBiFunction<? extends Double> remappingFunction, LongLongBiFunction<? extends Long> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(double, DoubleLongBiFunction, LongLongBiFunction)
     */
    @Deprecated
    default Long remapping(Double key, @NotNull BiFunction<? super Double, ? super Long, ? extends Double> remappingFunction, @NotNull BiFunction<? super Long, ? super Long, ? extends Long> mergeFunction) {
        return this.remapping(key != null ? key.doubleValue() : (double) 0, (double l_t, long l_u) -> remappingFunction.apply(l_t, l_u), (long l_t, long l_u) -> mergeFunction.apply(l_t, l_u));
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

    boolean containsValue(long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #containsValue(long)
     */
    @Deprecated
    default boolean containsValue(Object value) {
        if (value != null && !(value instanceof Long)) return false;
        return this.containsValue(value != null ? ((Long) value).longValue() : (long) 0);
    }

    void forEach(@NotNull DoubleLongBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(DoubleLongBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Double, ? super Long> action) {
        this.forEach((double l_t, long l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull DoubleLongBiOperator function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(DoubleLongBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Double, ? super Long, ? extends Long> function) {
        this.replaceAll((double l_t, long l_u) -> function.apply(l_t, l_u));
    }

    long getDefaultReturnValueRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getDefaultReturnValueRaw()
     */
    @Deprecated
    default Long getDefaultReturnValue() {
        return this.getDefaultReturnValueRaw();
    }

    long setDefaultReturnValue(long newDefault);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #setDefaultReturnValue(long)
     */
    @Deprecated
    default Long setDefaultReturnValue(Long newDefault) {
        return this.setDefaultReturnValue(newDefault != null ? newDefault.longValue() : (long) 0);
    }

    @NotNull
    DoubleSet keySet();

    @NotNull
    LongCollection values();

    @NotNull
    EntrySetExt<DoubleLongMap.Entry> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<MapExt.Entry<Double, Long>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface Entry extends MapExt.Entry<Double, Long> {
        double getKeyRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getKeyRaw()
         */
        @Deprecated
        default Double getKey() {
            return this.getKeyRaw();
        }

        long getValueRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getValueRaw()
         */
        @Deprecated
        default Long getValue() {
            return this.getValueRaw();
        }

        long setValue(long newValue);

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #setValue(long)
         */
        @Deprecated
        default Long setValue(Long value) {
            return this.setValue(value != null ? value.longValue() : (long) 0);
        }
    }

}
