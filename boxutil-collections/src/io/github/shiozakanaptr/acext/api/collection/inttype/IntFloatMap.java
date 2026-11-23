package io.github.shiozakanaptr.acext.api.collection.inttype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.floattype.FloatCollection;
import io.github.shiozakanaptr.acext.util.function.inttype.*;
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
public interface IntFloatMap extends MapExt<Integer, Float> {
    float get(int key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #get(int)
     */
    @Deprecated
    default Float get(Object key) {
        if (key != null && !(key instanceof Integer)) return this.getDefaultReturnValueRaw();
        return this.get(key != null ? ((Integer) key).intValue() : (int) 0);
    }

    float getOrDefault(int key, float defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(int, float)
     */
    @Deprecated
    default Float getOrDefault(Object key, Float defaultValue) {
        if (key != null && !(key instanceof Integer)) return defaultValue;
        return this.getOrDefault(key != null ? ((Integer) key).intValue() : (int) 0, defaultValue != null ? defaultValue.floatValue() : (float) 0);
    }

    float put(int key, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(int, float)
     */
    @Deprecated
    default Float put(Integer key, Float value) {
        return this.put(key != null ? key.intValue() : (int) 0, value != null ? value.floatValue() : (float) 0);
    }

    float putIfAbsent(int key, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(int, float)
     */
    @Deprecated
    default Float putIfAbsent(Integer key, Float value) {
        return this.putIfAbsent(key != null ? key.intValue() : (int) 0, value != null ? value.floatValue() : (float) 0);
    }

    float remove(int key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(int)
     */
    @Deprecated
    default Float remove(Object key) {
        if (key != null && !(key instanceof Integer)) return this.getDefaultReturnValueRaw();
        return this.remove(key != null ? ((Integer) key).intValue() : (int) 0);
    }

    boolean remove(int key, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(int, float)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Integer)) return false;
        if (value != null && !(value instanceof Float)) return false;
        return this.remove(key != null ? ((Integer) key).intValue() : (int) 0, value != null ? ((Float) value).floatValue() : (float) 0);
    }

    float replace(int key, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(int, float)
     */
    @Deprecated
    default Float replace(Integer key, Float value) {
        return this.replace(key != null ? key.intValue() : (int) 0, value != null ? value.floatValue() : (float) 0);
    }

    boolean replace(int key, float oldValue, float newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(int, float, float)
     */
    @Deprecated
    default boolean replace(Integer key, Float oldValue, Float newValue) {
        return this.replace(key != null ? key.intValue() : (int) 0, oldValue != null ? oldValue.floatValue() : (float) 0, newValue != null ? newValue.floatValue() : (float) 0);
    }

    float computeIfAbsent(int key, @NotNull IntFunction<? extends Float> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(int, IntFunction)
     */
    @Deprecated
    default Float computeIfAbsent(Integer key, @NotNull Function<? super Integer, ? extends Float> mappingFunction) {
        return this.computeIfAbsent(key != null ? key.intValue() : (int) 0, (Integer l_t) -> mappingFunction.apply(l_t));
    }

    float computeIfPresent(int key, @NotNull IntFloatBiFunction<? extends Float> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(int, IntFloatBiFunction)
     */
    @Deprecated
    default Float computeIfPresent(Integer key, @NotNull BiFunction<? super Integer, ? super Float, ? extends Float> remappingFunction) {
        return this.computeIfPresent(key != null ? key.intValue() : (int) 0, (int l_t, float l_u) -> remappingFunction.apply(l_t, l_u));
    }

    float compute(int key, @NotNull IntFloatBiFunction<? extends Float> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(int, IntFloatBiFunction)
     */
    @Deprecated
    default Float compute(Integer key, @NotNull BiFunction<? super Integer, ? super Float, ? extends Float> remappingFunction) {
        return this.compute(key != null ? key.intValue() : (int) 0, (int l_t, float l_u) -> remappingFunction.apply(l_t, l_u));
    }

    float merge(int key, float value, @NotNull FloatFloatBiFunction<? extends Float> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(int, float, FloatFloatBiFunction)
     */
    @Deprecated
    default Float merge(Integer key, @NotNull Float value, @NotNull BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
        return this.merge(key != null ? key.intValue() : (int) 0, value != null ? value.floatValue() : (float) 0, (float l_t, float l_u) -> remappingFunction.apply(l_t, l_u));
    }

    float update(int key, @NotNull FloatUnaryOperator updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(int, FloatUnaryOperator)
     */
    @Deprecated
    default Float update(Integer key, @NotNull Function<? super Float, ? extends Float> updateFunction) {
        return this.update(key != null ? key.intValue() : (int) 0, (float l_t) -> {
            final Float result;
            return (result = updateFunction.apply(l_t)) != null ? result : (float) 0;
        });
    }

    float remapping(int key, @NotNull IntFloatBiFunction<? extends Integer> remappingFunction, FloatFloatBiFunction<? extends Float> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(int, IntFloatBiFunction, FloatFloatBiFunction)
     */
    @Deprecated
    default Float remapping(Integer key, @NotNull BiFunction<? super Integer, ? super Float, ? extends Integer> remappingFunction, @NotNull BiFunction<? super Float, ? super Float, ? extends Float> mergeFunction) {
        return this.remapping(key != null ? key.intValue() : (int) 0, (int l_t, float l_u) -> remappingFunction.apply(l_t, l_u), (float l_t, float l_u) -> mergeFunction.apply(l_t, l_u));
    }

    boolean swapAt(int from, int to);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #swapAt(int, int)
     */
    @Deprecated
    default boolean swapAt(Integer from, Integer to) {
        return this.swapAt(from != null ? from.intValue() : (int) 0, to != null ? to.intValue() : (int) 0);
    }

    boolean containsKey(int key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #containsKey(int)
     */
    @Deprecated
    default boolean containsKey(Object key) {
        if (key != null && !(key instanceof Integer)) return false;
        return this.containsKey(key != null ? ((Integer) key).intValue() : (int) 0);
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

    void forEach(@NotNull IntFloatBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(IntFloatBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Integer, ? super Float> action) {
        this.forEach((int l_t, float l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull IntFloatBiOperator function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(IntFloatBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Integer, ? super Float, ? extends Float> function) {
        this.replaceAll((int l_t, float l_u) -> function.apply(l_t, l_u));
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
    IntSet keySet();

    @NotNull
    FloatCollection values();

    @NotNull
    EntrySetExt<IntFloatMap.Entry> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<MapExt.Entry<Integer, Float>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface Entry extends MapExt.Entry<Integer, Float> {
        int getKeyRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getKeyRaw()
         */
        @Deprecated
        default Integer getKey() {
            return this.getKeyRaw();
        }

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
