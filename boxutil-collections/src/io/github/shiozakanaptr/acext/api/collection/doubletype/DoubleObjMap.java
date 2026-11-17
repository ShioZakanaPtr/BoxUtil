package io.github.shiozakanaptr.acext.api.collection.doubletype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.doubletype.DoubleSet;
import io.github.shiozakanaptr.acext.api.collection.objecttype.ObjCollection;
import io.github.shiozakanaptr.acext.util.function.doubletype.*;
import io.github.shiozakanaptr.acext.util.function.objecttype.*;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.function.BiConsumer;

public interface DoubleObjMap<V> extends MapExt<Double, V> {
    V get(double key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #get(double)
     */
    @Deprecated
    default V get(Object key) {
        if (key != null && !(key instanceof Double)) return this.getDefaultReturnValue();
        return this.get(key != null ? ((Double) key).doubleValue() : (double) 0);
    }

    V getOrDefault(double key, V defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(double, V)
     */
    @Deprecated
    default V getOrDefault(Object key, V defaultValue) {
        if (key != null && !(key instanceof Double)) return defaultValue;
        return this.getOrDefault(key != null ? ((Double) key).doubleValue() : (double) 0, defaultValue);
    }

    V put(double key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(double, V)
     */
    @Deprecated
    default V put(Double key, V value) {
        return this.put(key != null ? key.doubleValue() : (double) 0, value);
    }

    V putIfAbsent(double key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(double, V)
     */
    @Deprecated
    default V putIfAbsent(Double key, V value) {
        return this.putIfAbsent(key != null ? key.doubleValue() : (double) 0, value);
    }

    V remove(double key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(double)
     */
    @Deprecated
    default V remove(Object key) {
        if (key != null && !(key instanceof Double)) return this.getDefaultReturnValue();
        return this.remove(key != null ? ((Double) key).doubleValue() : (double) 0);
    }

    boolean remove(double key, Object value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(double, Object)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Double)) return false;
        return this.remove(key != null ? ((Double) key).doubleValue() : (double) 0, value);
    }

    V replace(double key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(double, V)
     */
    @Deprecated
    default V replace(Double key, V value) {
        return this.replace(key != null ? key.doubleValue() : (double) 0, value);
    }

    boolean replace(double key, V oldValue, V newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(double, V, V)
     */
    @Deprecated
    default boolean replace(Double key, V oldValue, V newValue) {
        return this.replace(key != null ? key.doubleValue() : (double) 0, oldValue, newValue);
    }

    V computeIfAbsent(double key, @NotNull DoubleFunction<? extends V> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(double, DoubleFunction)
     */
    @Deprecated
    default V computeIfAbsent(Double key, @NotNull Function<? super Double, ? extends V> mappingFunction) {
        return this.computeIfAbsent(key != null ? key.doubleValue() : (double) 0, (Double l_t) -> mappingFunction.apply(l_t));
    }

    V computeIfPresent(double key, @NotNull DoubleObjBiFunction<? super V, ? extends V> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(double, DoubleObjBiFunction)
     */
    @Deprecated
    default V computeIfPresent(Double key, @NotNull BiFunction<? super Double, ? super V, ? extends V> remappingFunction) {
        return this.computeIfPresent(key != null ? key.doubleValue() : (double) 0, (double l_t, V l_u) -> remappingFunction.apply(l_t, l_u));
    }

    V compute(double key, @NotNull DoubleObjBiFunction<? super V, ? extends V> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(double, DoubleObjBiFunction)
     */
    @Deprecated
    default V compute(Double key, @NotNull BiFunction<? super Double, ? super V, ? extends V> remappingFunction) {
        return this.compute(key != null ? key.doubleValue() : (double) 0, (double l_t, V l_u) -> remappingFunction.apply(l_t, l_u));
    }

    V merge(double key, V value, @NotNull BiFunction<? super V, ? super V, ? extends V> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(double, V, BiFunction)
     */
    @Deprecated
    default V merge(Double key, @NotNull V value, @NotNull BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return this.merge(key != null ? key.doubleValue() : (double) 0, value, (V l_t, V l_u) -> remappingFunction.apply(l_t, l_u));
    }

    V update(double key, @NotNull Function<? super V, ? extends V> updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(double, Function)
     */
    @Deprecated
    default V update(Double key, @NotNull Function<? super V, ? extends V> updateFunction) {
        return this.update(key != null ? key.doubleValue() : (double) 0, (V l_t) -> updateFunction.apply(l_t));
    }

    V remapping(double key, @NotNull DoubleObjBiFunction<? super V, ? extends Double> remappingFunction, BiFunction<? super V, ? super V, ? extends V> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(double, DoubleObjBiFunction, BiFunction)
     */
    @Deprecated
    default V remapping(Double key, @NotNull BiFunction<? super Double, ? super V, ? extends Double> remappingFunction, @NotNull BiFunction<? super V, ? super V, ? extends V> mergeFunction) {
        return this.remapping(key != null ? key.doubleValue() : (double) 0, (double l_t, V l_u) -> remappingFunction.apply(l_t, l_u), (V l_t, V l_u) -> mergeFunction.apply(l_t, l_u));
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

    void forEach(@NotNull DoubleObjBiConsumer<? super V> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(DoubleObjBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Double, ? super V> action) {
        this.forEach((double l_t, V l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull DoubleObjBiFunction<? super V, ? extends V> function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(DoubleObjBiFunction)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Double, ? super V, ? extends V> function) {
        this.replaceAll((double l_t, V l_u) -> function.apply(l_t, l_u));
    }

    @NotNull
    DoubleSet keySet();

    @NotNull
    ObjCollection<V> values();

    @NotNull
    EntrySetExt<DoubleObjMap.Entry<V>> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<MapExt.Entry<Double, V>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface Entry<V> extends MapExt.Entry<Double, V> {
        double getKeyRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getKeyRaw()
         */
        @Deprecated
        default Double getKey() {
            return this.getKeyRaw();
        }

    }

}
