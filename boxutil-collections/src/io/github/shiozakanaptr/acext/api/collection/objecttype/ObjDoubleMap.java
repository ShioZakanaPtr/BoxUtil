package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.doubletype.DoubleCollection;
import io.github.shiozakanaptr.acext.util.function.objecttype.*;
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
public interface ObjDoubleMap<K> extends MapExt<K, Double> {
    double getRaw(Object key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getRaw(Object)
     */
    @Deprecated
    default Double get(Object key) {
        return this.getRaw(key);
    }

    double getOrDefault(Object key, double defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(Object, double)
     */
    @Deprecated
    default Double getOrDefault(Object key, Double defaultValue) {
        return this.getOrDefault(key, defaultValue != null ? defaultValue.doubleValue() : (double) 0);
    }

    double put(K key, double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(K, double)
     */
    @Deprecated
    default Double put(K key, Double value) {
        return this.put(key, value != null ? value.doubleValue() : (double) 0);
    }

    double putIfAbsent(K key, double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(K, double)
     */
    @Deprecated
    default Double putIfAbsent(K key, Double value) {
        return this.putIfAbsent(key, value != null ? value.doubleValue() : (double) 0);
    }

    double removeRaw(Object key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeRaw(Object)
     */
    @Deprecated
    default Double remove(Object key) {
        return this.removeRaw(key);
    }

    boolean remove(Object key, double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(Object, double)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (value != null && !(value instanceof Double)) return false;
        return this.remove(key, value != null ? ((Double) value).doubleValue() : (double) 0);
    }

    double replace(K key, double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(K, double)
     */
    @Deprecated
    default Double replace(K key, Double value) {
        return this.replace(key, value != null ? value.doubleValue() : (double) 0);
    }

    boolean replace(K key, double oldValue, double newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(K, double, double)
     */
    @Deprecated
    default boolean replace(K key, Double oldValue, Double newValue) {
        return this.replace(key, oldValue != null ? oldValue.doubleValue() : (double) 0, newValue != null ? newValue.doubleValue() : (double) 0);
    }

    double computeIfAbsentRaw(K key, @NotNull Function<? super K, ? extends Double> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsentRaw(K, Function)
     */
    @Deprecated
    default Double computeIfAbsent(K key, @NotNull Function<? super K, ? extends Double> mappingFunction) {
        return this.computeIfAbsentRaw(key, (K l_t) -> mappingFunction.apply(l_t));
    }

    double computeIfPresent(K key, @NotNull ObjDoubleBiFunction<? super K, ? extends Double> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(K, ObjDoubleBiFunction)
     */
    @Deprecated
    default Double computeIfPresent(K key, @NotNull BiFunction<? super K, ? super Double, ? extends Double> remappingFunction) {
        return this.computeIfPresent(key, (K l_t, double l_u) -> remappingFunction.apply(l_t, l_u));
    }

    double compute(K key, @NotNull ObjDoubleBiFunction<? super K, ? extends Double> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(K, ObjDoubleBiFunction)
     */
    @Deprecated
    default Double compute(K key, @NotNull BiFunction<? super K, ? super Double, ? extends Double> remappingFunction) {
        return this.compute(key, (K l_t, double l_u) -> remappingFunction.apply(l_t, l_u));
    }

    double merge(K key, double value, @NotNull DoubleDoubleBiFunction<? extends Double> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(K, double, DoubleDoubleBiFunction)
     */
    @Deprecated
    default Double merge(K key, @NotNull Double value, @NotNull BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
        return this.merge(key, value != null ? value.doubleValue() : (double) 0, (double l_t, double l_u) -> remappingFunction.apply(l_t, l_u));
    }

    double update(K key, @NotNull DoubleUnaryOperator updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(K, DoubleUnaryOperator)
     */
    @Deprecated
    default Double update(K key, @NotNull Function<? super Double, ? extends Double> updateFunction) {
        return this.update(key, (double l_t) -> {
            final Double result;
            return (result = updateFunction.apply(l_t)) != null ? result : (double) 0;
        });
    }

    double remapping(K key, @NotNull ObjDoubleBiFunction<? super K, ? extends K> remappingFunction, DoubleDoubleBiFunction<? extends Double> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(K, ObjDoubleBiFunction, DoubleDoubleBiFunction)
     */
    @Deprecated
    default Double remapping(K key, @NotNull BiFunction<? super K, ? super Double, ? extends K> remappingFunction, @NotNull BiFunction<? super Double, ? super Double, ? extends Double> mergeFunction) {
        return this.remapping(key, (K l_t, double l_u) -> remappingFunction.apply(l_t, l_u), (double l_t, double l_u) -> mergeFunction.apply(l_t, l_u));
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

    void forEach(@NotNull ObjDoubleBiConsumer<? super K> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(ObjDoubleBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super K, ? super Double> action) {
        this.forEach((K l_t, double l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull ObjDoubleBiOperator<? super K> function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(ObjDoubleBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super K, ? super Double, ? extends Double> function) {
        this.replaceAll((K l_t, double l_u) -> function.apply(l_t, l_u));
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
    ObjSet<K> keySet();

    @NotNull
    DoubleCollection values();

    @NotNull
    EntrySetExt<ObjDoubleMap.Entry<K>> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<MapExt.Entry<K, Double>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface Entry<K> extends MapExt.Entry<K, Double> {

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
