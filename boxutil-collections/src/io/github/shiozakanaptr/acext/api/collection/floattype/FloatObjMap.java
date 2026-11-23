package io.github.shiozakanaptr.acext.api.collection.floattype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.objecttype.ObjCollection;
import io.github.shiozakanaptr.acext.util.function.floattype.*;
import io.github.shiozakanaptr.acext.util.function.objecttype.*;
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
public interface FloatObjMap<V> extends MapExt<Float, V> {
    V get(float key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #get(float)
     */
    @Deprecated
    default V get(Object key) {
        if (key != null && !(key instanceof Float)) return this.getDefaultReturnValue();
        return this.get(key != null ? ((Float) key).floatValue() : (float) 0);
    }

    V getOrDefault(float key, V defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(float, V)
     */
    @Deprecated
    default V getOrDefault(Object key, V defaultValue) {
        if (key != null && !(key instanceof Float)) return defaultValue;
        return this.getOrDefault(key != null ? ((Float) key).floatValue() : (float) 0, defaultValue);
    }

    V put(float key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(float, V)
     */
    @Deprecated
    default V put(Float key, V value) {
        return this.put(key != null ? key.floatValue() : (float) 0, value);
    }

    V putIfAbsent(float key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(float, V)
     */
    @Deprecated
    default V putIfAbsent(Float key, V value) {
        return this.putIfAbsent(key != null ? key.floatValue() : (float) 0, value);
    }

    V remove(float key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(float)
     */
    @Deprecated
    default V remove(Object key) {
        if (key != null && !(key instanceof Float)) return this.getDefaultReturnValue();
        return this.remove(key != null ? ((Float) key).floatValue() : (float) 0);
    }

    boolean remove(float key, Object value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(float, Object)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Float)) return false;
        return this.remove(key != null ? ((Float) key).floatValue() : (float) 0, value);
    }

    V replace(float key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(float, V)
     */
    @Deprecated
    default V replace(Float key, V value) {
        return this.replace(key != null ? key.floatValue() : (float) 0, value);
    }

    boolean replace(float key, V oldValue, V newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(float, V, V)
     */
    @Deprecated
    default boolean replace(Float key, V oldValue, V newValue) {
        return this.replace(key != null ? key.floatValue() : (float) 0, oldValue, newValue);
    }

    V computeIfAbsent(float key, @NotNull FloatFunction<? extends V> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(float, FloatFunction)
     */
    @Deprecated
    default V computeIfAbsent(Float key, @NotNull Function<? super Float, ? extends V> mappingFunction) {
        return this.computeIfAbsent(key != null ? key.floatValue() : (float) 0, (Float l_t) -> mappingFunction.apply(l_t));
    }

    V computeIfPresent(float key, @NotNull FloatObjBiFunction<? super V, ? extends V> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(float, FloatObjBiFunction)
     */
    @Deprecated
    default V computeIfPresent(Float key, @NotNull BiFunction<? super Float, ? super V, ? extends V> remappingFunction) {
        return this.computeIfPresent(key != null ? key.floatValue() : (float) 0, (float l_t, V l_u) -> remappingFunction.apply(l_t, l_u));
    }

    V compute(float key, @NotNull FloatObjBiFunction<? super V, ? extends V> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(float, FloatObjBiFunction)
     */
    @Deprecated
    default V compute(Float key, @NotNull BiFunction<? super Float, ? super V, ? extends V> remappingFunction) {
        return this.compute(key != null ? key.floatValue() : (float) 0, (float l_t, V l_u) -> remappingFunction.apply(l_t, l_u));
    }

    V merge(float key, V value, @NotNull BiFunction<? super V, ? super V, ? extends V> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(float, V, BiFunction)
     */
    @Deprecated
    default V merge(Float key, @NotNull V value, @NotNull BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return this.merge(key != null ? key.floatValue() : (float) 0, value, (V l_t, V l_u) -> remappingFunction.apply(l_t, l_u));
    }

    V update(float key, @NotNull Function<? super V, ? extends V> updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(float, Function)
     */
    @Deprecated
    default V update(Float key, @NotNull Function<? super V, ? extends V> updateFunction) {
        return this.update(key != null ? key.floatValue() : (float) 0, (V l_t) -> updateFunction.apply(l_t));
    }

    V remapping(float key, @NotNull FloatObjBiFunction<? super V, ? extends Float> remappingFunction, BiFunction<? super V, ? super V, ? extends V> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(float, FloatObjBiFunction, BiFunction)
     */
    @Deprecated
    default V remapping(Float key, @NotNull BiFunction<? super Float, ? super V, ? extends Float> remappingFunction, @NotNull BiFunction<? super V, ? super V, ? extends V> mergeFunction) {
        return this.remapping(key != null ? key.floatValue() : (float) 0, (float l_t, V l_u) -> remappingFunction.apply(l_t, l_u), (V l_t, V l_u) -> mergeFunction.apply(l_t, l_u));
    }

    boolean swapAt(float from, float to);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #swapAt(float, float)
     */
    @Deprecated
    default boolean swapAt(Float from, Float to) {
        return this.swapAt(from != null ? from.floatValue() : (float) 0, to != null ? to.floatValue() : (float) 0);
    }

    boolean containsKey(float key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #containsKey(float)
     */
    @Deprecated
    default boolean containsKey(Object key) {
        if (key != null && !(key instanceof Float)) return false;
        return this.containsKey(key != null ? ((Float) key).floatValue() : (float) 0);
    }

    void forEach(@NotNull FloatObjBiConsumer<? super V> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(FloatObjBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Float, ? super V> action) {
        this.forEach((float l_t, V l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull FloatObjBiFunction<? super V, ? extends V> function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(FloatObjBiFunction)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Float, ? super V, ? extends V> function) {
        this.replaceAll((float l_t, V l_u) -> function.apply(l_t, l_u));
    }

    @NotNull
    FloatSet keySet();

    @NotNull
    ObjCollection<V> values();

    @NotNull
    EntrySetExt<FloatObjMap.Entry<V>> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<MapExt.Entry<Float, V>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface Entry<V> extends MapExt.Entry<Float, V> {
        float getKeyRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getKeyRaw()
         */
        @Deprecated
        default Float getKey() {
            return this.getKeyRaw();
        }

    }

}
