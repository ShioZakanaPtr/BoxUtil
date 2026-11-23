package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.floattype.FloatCollection;
import io.github.shiozakanaptr.acext.util.function.objecttype.*;
import io.github.shiozakanaptr.acext.util.function.floattype.*;
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
public interface ObjFloatMap<K> extends MapExt<K, Float> {
    float getRaw(Object key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getRaw(Object)
     */
    @Deprecated
    default Float get(Object key) {
        return this.getRaw(key);
    }

    float getOrDefault(Object key, float defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(Object, float)
     */
    @Deprecated
    default Float getOrDefault(Object key, Float defaultValue) {
        return this.getOrDefault(key, defaultValue != null ? defaultValue.floatValue() : (float) 0);
    }

    float put(K key, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(K, float)
     */
    @Deprecated
    default Float put(K key, Float value) {
        return this.put(key, value != null ? value.floatValue() : (float) 0);
    }

    float putIfAbsent(K key, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(K, float)
     */
    @Deprecated
    default Float putIfAbsent(K key, Float value) {
        return this.putIfAbsent(key, value != null ? value.floatValue() : (float) 0);
    }

    float removeRaw(Object key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeRaw(Object)
     */
    @Deprecated
    default Float remove(Object key) {
        return this.removeRaw(key);
    }

    boolean remove(Object key, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(Object, float)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (value != null && !(value instanceof Float)) return false;
        return this.remove(key, value != null ? ((Float) value).floatValue() : (float) 0);
    }

    float replace(K key, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(K, float)
     */
    @Deprecated
    default Float replace(K key, Float value) {
        return this.replace(key, value != null ? value.floatValue() : (float) 0);
    }

    boolean replace(K key, float oldValue, float newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(K, float, float)
     */
    @Deprecated
    default boolean replace(K key, Float oldValue, Float newValue) {
        return this.replace(key, oldValue != null ? oldValue.floatValue() : (float) 0, newValue != null ? newValue.floatValue() : (float) 0);
    }

    float computeIfAbsentRaw(K key, @NotNull Function<? super K, ? extends Float> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsentRaw(K, Function)
     */
    @Deprecated
    default Float computeIfAbsent(K key, @NotNull Function<? super K, ? extends Float> mappingFunction) {
        return this.computeIfAbsentRaw(key, (K l_t) -> mappingFunction.apply(l_t));
    }

    float computeIfPresent(K key, @NotNull ObjFloatBiFunction<? super K, ? extends Float> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(K, ObjFloatBiFunction)
     */
    @Deprecated
    default Float computeIfPresent(K key, @NotNull BiFunction<? super K, ? super Float, ? extends Float> remappingFunction) {
        return this.computeIfPresent(key, (K l_t, float l_u) -> remappingFunction.apply(l_t, l_u));
    }

    float compute(K key, @NotNull ObjFloatBiFunction<? super K, ? extends Float> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(K, ObjFloatBiFunction)
     */
    @Deprecated
    default Float compute(K key, @NotNull BiFunction<? super K, ? super Float, ? extends Float> remappingFunction) {
        return this.compute(key, (K l_t, float l_u) -> remappingFunction.apply(l_t, l_u));
    }

    float merge(K key, float value, @NotNull FloatFloatBiFunction<? extends Float> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(K, float, FloatFloatBiFunction)
     */
    @Deprecated
    default Float merge(K key, @NotNull Float value, @NotNull BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
        return this.merge(key, value.floatValue(), (float l_t, float l_u) -> remappingFunction.apply(l_t, l_u));
    }

    float update(K key, @NotNull FloatUnaryOperator updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(K, FloatUnaryOperator)
     */
    @Deprecated
    default Float update(K key, @NotNull Function<? super Float, ? extends Float> updateFunction) {
        return this.update(key, (float l_t) -> {
            final Float result;
            return (result = updateFunction.apply(l_t)) != null ? result : (float) 0;
        });
    }

    float remapping(K key, @NotNull ObjFloatBiFunction<? super K, ? extends K> remappingFunction, FloatFloatBiFunction<? extends Float> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(K, ObjFloatBiFunction, FloatFloatBiFunction)
     */
    @Deprecated
    default Float remapping(K key, @NotNull BiFunction<? super K, ? super Float, ? extends K> remappingFunction, @NotNull BiFunction<? super Float, ? super Float, ? extends Float> mergeFunction) {
        return this.remapping(key, (K l_t, float l_u) -> remappingFunction.apply(l_t, l_u), (float l_t, float l_u) -> mergeFunction.apply(l_t, l_u));
    }

    boolean containsValue(float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #containsValue(float)
     */
    @Deprecated
    default boolean containsValue(Object value) {
        if (value != null && !(value instanceof Float)) return false;
        return this.containsValue(value != null ? ((Float) value).floatValue() : (float) 0);
    }

    void forEach(@NotNull ObjFloatBiConsumer<? super K> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(ObjFloatBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super K, ? super Float> action) {
        this.forEach((K l_t, float l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull ObjFloatBiOperator<? super K> function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(ObjFloatBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super K, ? super Float, ? extends Float> function) {
        this.replaceAll((K l_t, float l_u) -> function.apply(l_t, l_u));
    }

    float getDefaultReturnValueRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getDefaultReturnValueRaw()
     */
    @Deprecated
    default Float getDefaultReturnValue() {
        return this.getDefaultReturnValueRaw();
    }

    float setDefaultReturnValue(float newDefault);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #setDefaultReturnValue(float)
     */
    @Deprecated
    default Float setDefaultReturnValue(Float newDefault) {
        return this.setDefaultReturnValue(newDefault != null ? newDefault.floatValue() : (float) 0);
    }

    @NotNull
    ObjSet<K> keySet();

    @NotNull
    FloatCollection values();

    @NotNull
    EntrySetExt<ObjFloatMap.Entry<K>> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<MapExt.Entry<K, Float>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface Entry<K> extends MapExt.Entry<K, Float> {
        float getValueRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getValueRaw()
         */
        @Deprecated
        default Float getValue() {
            return this.getValueRaw();
        }

        float setValue(float newValue);

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #setValue(float)
         */
        @Deprecated
        default Float setValue(Float value) {
            return this.setValue(value != null ? value.floatValue() : (float) 0);
        }
    }

}
