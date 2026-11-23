package io.github.shiozakanaptr.acext.api.collection.floattype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.longtype.LongCollection;
import io.github.shiozakanaptr.acext.util.function.floattype.*;
import io.github.shiozakanaptr.acext.util.function.longtype.*;
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
public interface FloatLongMap extends MapExt<Float, Long> {
    long get(float key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #get(float)
     */
    @Deprecated
    default Long get(Object key) {
        if (key != null && !(key instanceof Float)) return this.getDefaultReturnValueRaw();
        return this.get(key != null ? ((Float) key).floatValue() : (float) 0);
    }

    long getOrDefault(float key, long defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(float, long)
     */
    @Deprecated
    default Long getOrDefault(Object key, Long defaultValue) {
        if (key != null && !(key instanceof Float)) return defaultValue;
        return this.getOrDefault(key != null ? ((Float) key).floatValue() : (float) 0, defaultValue != null ? defaultValue.longValue() : (long) 0);
    }

    long put(float key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(float, long)
     */
    @Deprecated
    default Long put(Float key, Long value) {
        return this.put(key != null ? key.floatValue() : (float) 0, value != null ? value.longValue() : (long) 0);
    }

    long putIfAbsent(float key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(float, long)
     */
    @Deprecated
    default Long putIfAbsent(Float key, Long value) {
        return this.putIfAbsent(key != null ? key.floatValue() : (float) 0, value != null ? value.longValue() : (long) 0);
    }

    long remove(float key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(float)
     */
    @Deprecated
    default Long remove(Object key) {
        if (key != null && !(key instanceof Float)) return this.getDefaultReturnValueRaw();
        return this.remove(key != null ? ((Float) key).floatValue() : (float) 0);
    }

    boolean remove(float key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(float, long)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Float)) return false;
        if (value != null && !(value instanceof Long)) return false;
        return this.remove(key != null ? ((Float) key).floatValue() : (float) 0, value != null ? ((Long) value).longValue() : (long) 0);
    }

    long replace(float key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(float, long)
     */
    @Deprecated
    default Long replace(Float key, Long value) {
        return this.replace(key != null ? key.floatValue() : (float) 0, value != null ? value.longValue() : (long) 0);
    }

    boolean replace(float key, long oldValue, long newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(float, long, long)
     */
    @Deprecated
    default boolean replace(Float key, Long oldValue, Long newValue) {
        return this.replace(key != null ? key.floatValue() : (float) 0, oldValue != null ? oldValue.longValue() : (long) 0, newValue != null ? newValue.longValue() : (long) 0);
    }

    long computeIfAbsent(float key, @NotNull FloatFunction<? extends Long> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(float, FloatFunction)
     */
    @Deprecated
    default Long computeIfAbsent(Float key, @NotNull Function<? super Float, ? extends Long> mappingFunction) {
        return this.computeIfAbsent(key != null ? key.floatValue() : (float) 0, (Float l_t) -> mappingFunction.apply(l_t));
    }

    long computeIfPresent(float key, @NotNull FloatLongBiFunction<? extends Long> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(float, FloatLongBiFunction)
     */
    @Deprecated
    default Long computeIfPresent(Float key, @NotNull BiFunction<? super Float, ? super Long, ? extends Long> remappingFunction) {
        return this.computeIfPresent(key != null ? key.floatValue() : (float) 0, (float l_t, long l_u) -> remappingFunction.apply(l_t, l_u));
    }

    long compute(float key, @NotNull FloatLongBiFunction<? extends Long> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(float, FloatLongBiFunction)
     */
    @Deprecated
    default Long compute(Float key, @NotNull BiFunction<? super Float, ? super Long, ? extends Long> remappingFunction) {
        return this.compute(key != null ? key.floatValue() : (float) 0, (float l_t, long l_u) -> remappingFunction.apply(l_t, l_u));
    }

    long merge(float key, long value, @NotNull LongLongBiFunction<? extends Long> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(float, long, LongLongBiFunction)
     */
    @Deprecated
    default Long merge(Float key, @NotNull Long value, @NotNull BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
        return this.merge(key != null ? key.floatValue() : (float) 0, value != null ? value.longValue() : (long) 0, (long l_t, long l_u) -> remappingFunction.apply(l_t, l_u));
    }

    long update(float key, @NotNull LongUnaryOperator updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(float, LongUnaryOperator)
     */
    @Deprecated
    default Long update(Float key, @NotNull Function<? super Long, ? extends Long> updateFunction) {
        return this.update(key != null ? key.floatValue() : (float) 0, (long l_t) -> {
            final Long result;
            return (result = updateFunction.apply(l_t)) != null ? result : (long) 0;
        });
    }

    long remapping(float key, @NotNull FloatLongBiFunction<? extends Float> remappingFunction, LongLongBiFunction<? extends Long> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(float, FloatLongBiFunction, LongLongBiFunction)
     */
    @Deprecated
    default Long remapping(Float key, @NotNull BiFunction<? super Float, ? super Long, ? extends Float> remappingFunction, @NotNull BiFunction<? super Long, ? super Long, ? extends Long> mergeFunction) {
        return this.remapping(key != null ? key.floatValue() : (float) 0, (float l_t, long l_u) -> remappingFunction.apply(l_t, l_u), (long l_t, long l_u) -> mergeFunction.apply(l_t, l_u));
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

    void forEach(@NotNull FloatLongBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(FloatLongBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Float, ? super Long> action) {
        this.forEach((float l_t, long l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull FloatLongBiOperator function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(FloatLongBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Float, ? super Long, ? extends Long> function) {
        this.replaceAll((float l_t, long l_u) -> function.apply(l_t, l_u));
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
    FloatSet keySet();

    @NotNull
    LongCollection values();

    @NotNull
    EntrySetExt<FloatLongMap.Entry> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<MapExt.Entry<Float, Long>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface Entry extends MapExt.Entry<Float, Long> {
        float getKeyRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getKeyRaw()
         */
        @Deprecated
        default Float getKey() {
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
