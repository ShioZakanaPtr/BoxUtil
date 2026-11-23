package io.github.shiozakanaptr.acext.api.collection.longtype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.floattype.FloatCollection;
import io.github.shiozakanaptr.acext.util.function.longtype.*;
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
public interface LongFloatMap extends MapExt<Long, Float> {
    float get(long key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #get(long)
     */
    @Deprecated
    default Float get(Object key) {
        if (key != null && !(key instanceof Long)) return this.getDefaultReturnValueRaw();
        return this.get(key != null ? ((Long) key).longValue() : (long) 0);
    }

    float getOrDefault(long key, float defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(long, float)
     */
    @Deprecated
    default Float getOrDefault(Object key, Float defaultValue) {
        if (key != null && !(key instanceof Long)) return defaultValue;
        return this.getOrDefault(key != null ? ((Long) key).longValue() : (long) 0, defaultValue != null ? defaultValue.floatValue() : (float) 0);
    }

    float put(long key, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(long, float)
     */
    @Deprecated
    default Float put(Long key, Float value) {
        return this.put(key != null ? key.longValue() : (long) 0, value != null ? value.floatValue() : (float) 0);
    }

    float putIfAbsent(long key, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(long, float)
     */
    @Deprecated
    default Float putIfAbsent(Long key, Float value) {
        return this.putIfAbsent(key != null ? key.longValue() : (long) 0, value != null ? value.floatValue() : (float) 0);
    }

    float remove(long key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(long)
     */
    @Deprecated
    default Float remove(Object key) {
        if (key != null && !(key instanceof Long)) return this.getDefaultReturnValueRaw();
        return this.remove(key != null ? ((Long) key).longValue() : (long) 0);
    }

    boolean remove(long key, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(long, float)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Long)) return false;
        if (value != null && !(value instanceof Float)) return false;
        return this.remove(key != null ? ((Long) key).longValue() : (long) 0, value != null ? ((Float) value).floatValue() : (float) 0);
    }

    float replace(long key, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(long, float)
     */
    @Deprecated
    default Float replace(Long key, Float value) {
        return this.replace(key != null ? key.longValue() : (long) 0, value != null ? value.floatValue() : (float) 0);
    }

    boolean replace(long key, float oldValue, float newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(long, float, float)
     */
    @Deprecated
    default boolean replace(Long key, Float oldValue, Float newValue) {
        return this.replace(key != null ? key.longValue() : (long) 0, oldValue != null ? oldValue.floatValue() : (float) 0, newValue != null ? newValue.floatValue() : (float) 0);
    }

    float computeIfAbsent(long key, @NotNull LongFunction<? extends Float> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(long, LongFunction)
     */
    @Deprecated
    default Float computeIfAbsent(Long key, @NotNull Function<? super Long, ? extends Float> mappingFunction) {
        return this.computeIfAbsent(key != null ? key.longValue() : (long) 0, (Long l_t) -> mappingFunction.apply(l_t));
    }

    float computeIfPresent(long key, @NotNull LongFloatBiFunction<? extends Float> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(long, LongFloatBiFunction)
     */
    @Deprecated
    default Float computeIfPresent(Long key, @NotNull BiFunction<? super Long, ? super Float, ? extends Float> remappingFunction) {
        return this.computeIfPresent(key != null ? key.longValue() : (long) 0, (long l_t, float l_u) -> remappingFunction.apply(l_t, l_u));
    }

    float compute(long key, @NotNull LongFloatBiFunction<? extends Float> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(long, LongFloatBiFunction)
     */
    @Deprecated
    default Float compute(Long key, @NotNull BiFunction<? super Long, ? super Float, ? extends Float> remappingFunction) {
        return this.compute(key != null ? key.longValue() : (long) 0, (long l_t, float l_u) -> remappingFunction.apply(l_t, l_u));
    }

    float merge(long key, float value, @NotNull FloatFloatBiFunction<? extends Float> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(long, float, FloatFloatBiFunction)
     */
    @Deprecated
    default Float merge(Long key, @NotNull Float value, @NotNull BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
        return this.merge(key != null ? key.longValue() : (long) 0, value != null ? value.floatValue() : (float) 0, (float l_t, float l_u) -> remappingFunction.apply(l_t, l_u));
    }

    float update(long key, @NotNull FloatUnaryOperator updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(long, FloatUnaryOperator)
     */
    @Deprecated
    default Float update(Long key, @NotNull Function<? super Float, ? extends Float> updateFunction) {
        return this.update(key != null ? key.longValue() : (long) 0, (float l_t) -> {
            final Float result;
            return (result = updateFunction.apply(l_t)) != null ? result : (float) 0;
        });
    }

    float remapping(long key, @NotNull LongFloatBiFunction<? extends Long> remappingFunction, FloatFloatBiFunction<? extends Float> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(long, LongFloatBiFunction, FloatFloatBiFunction)
     */
    @Deprecated
    default Float remapping(Long key, @NotNull BiFunction<? super Long, ? super Float, ? extends Long> remappingFunction, @NotNull BiFunction<? super Float, ? super Float, ? extends Float> mergeFunction) {
        return this.remapping(key != null ? key.longValue() : (long) 0, (long l_t, float l_u) -> remappingFunction.apply(l_t, l_u), (float l_t, float l_u) -> mergeFunction.apply(l_t, l_u));
    }

    boolean swapAt(long from, long to);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #swapAt(long, long)
     */
    @Deprecated
    default boolean swapAt(Long from, Long to) {
        return this.swapAt(from != null ? from.longValue() : (long) 0, to != null ? to.longValue() : (long) 0);
    }

    boolean containsKey(long key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #containsKey(long)
     */
    @Deprecated
    default boolean containsKey(Object key) {
        if (key != null && !(key instanceof Long)) return false;
        return this.containsKey(key != null ? ((Long) key).longValue() : (long) 0);
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

    void forEach(@NotNull LongFloatBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(LongFloatBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Long, ? super Float> action) {
        this.forEach((long l_t, float l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull LongFloatBiOperator function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(LongFloatBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Long, ? super Float, ? extends Float> function) {
        this.replaceAll((long l_t, float l_u) -> function.apply(l_t, l_u));
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
    LongSet keySet();

    @NotNull
    FloatCollection values();

    @NotNull
    EntrySetExt<LongFloatMap.Entry> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<MapExt.Entry<Long, Float>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface Entry extends MapExt.Entry<Long, Float> {
        long getKeyRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getKeyRaw()
         */
        @Deprecated
        default Long getKey() {
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
