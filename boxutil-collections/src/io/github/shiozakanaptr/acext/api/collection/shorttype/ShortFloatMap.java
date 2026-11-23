package io.github.shiozakanaptr.acext.api.collection.shorttype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.floattype.FloatCollection;
import io.github.shiozakanaptr.acext.util.function.shorttype.*;
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
public interface ShortFloatMap extends MapExt<Short, Float> {
    float get(short key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #get(short)
     */
    @Deprecated
    default Float get(Object key) {
        if (key != null && !(key instanceof Short)) return this.getDefaultReturnValueRaw();
        return this.get(key != null ? ((Short) key).shortValue() : (short) 0);
    }

    float getOrDefault(short key, float defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(short, float)
     */
    @Deprecated
    default Float getOrDefault(Object key, Float defaultValue) {
        if (key != null && !(key instanceof Short)) return defaultValue;
        return this.getOrDefault(key != null ? ((Short) key).shortValue() : (short) 0, defaultValue != null ? defaultValue.floatValue() : (float) 0);
    }

    float put(short key, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(short, float)
     */
    @Deprecated
    default Float put(Short key, Float value) {
        return this.put(key != null ? key.shortValue() : (short) 0, value != null ? value.floatValue() : (float) 0);
    }

    float putIfAbsent(short key, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(short, float)
     */
    @Deprecated
    default Float putIfAbsent(Short key, Float value) {
        return this.putIfAbsent(key != null ? key.shortValue() : (short) 0, value != null ? value.floatValue() : (float) 0);
    }

    float remove(short key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(short)
     */
    @Deprecated
    default Float remove(Object key) {
        if (key != null && !(key instanceof Short)) return this.getDefaultReturnValueRaw();
        return this.remove(key != null ? ((Short) key).shortValue() : (short) 0);
    }

    boolean remove(short key, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(short, float)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Short)) return false;
        if (value != null && !(value instanceof Float)) return false;
        return this.remove(key != null ? ((Short) key).shortValue() : (short) 0, value != null ? ((Float) value).floatValue() : (float) 0);
    }

    float replace(short key, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(short, float)
     */
    @Deprecated
    default Float replace(Short key, Float value) {
        return this.replace(key != null ? key.shortValue() : (short) 0, value != null ? value.floatValue() : (float) 0);
    }

    boolean replace(short key, float oldValue, float newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(short, float, float)
     */
    @Deprecated
    default boolean replace(Short key, Float oldValue, Float newValue) {
        return this.replace(key != null ? key.shortValue() : (short) 0, oldValue != null ? oldValue.floatValue() : (float) 0, newValue != null ? newValue.floatValue() : (float) 0);
    }

    float computeIfAbsent(short key, @NotNull ShortFunction<? extends Float> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(short, ShortFunction)
     */
    @Deprecated
    default Float computeIfAbsent(Short key, @NotNull Function<? super Short, ? extends Float> mappingFunction) {
        return this.computeIfAbsent(key != null ? key.shortValue() : (short) 0, (Short l_t) -> mappingFunction.apply(l_t));
    }

    float computeIfPresent(short key, @NotNull ShortFloatBiFunction<? extends Float> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(short, ShortFloatBiFunction)
     */
    @Deprecated
    default Float computeIfPresent(Short key, @NotNull BiFunction<? super Short, ? super Float, ? extends Float> remappingFunction) {
        return this.computeIfPresent(key != null ? key.shortValue() : (short) 0, (short l_t, float l_u) -> remappingFunction.apply(l_t, l_u));
    }

    float compute(short key, @NotNull ShortFloatBiFunction<? extends Float> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(short, ShortFloatBiFunction)
     */
    @Deprecated
    default Float compute(Short key, @NotNull BiFunction<? super Short, ? super Float, ? extends Float> remappingFunction) {
        return this.compute(key != null ? key.shortValue() : (short) 0, (short l_t, float l_u) -> remappingFunction.apply(l_t, l_u));
    }

    float merge(short key, float value, @NotNull FloatFloatBiFunction<? extends Float> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(short, float, FloatFloatBiFunction)
     */
    @Deprecated
    default Float merge(Short key, @NotNull Float value, @NotNull BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
        return this.merge(key != null ? key.shortValue() : (short) 0, value != null ? value.floatValue() : (float) 0, (float l_t, float l_u) -> remappingFunction.apply(l_t, l_u));
    }

    float update(short key, @NotNull FloatUnaryOperator updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(short, FloatUnaryOperator)
     */
    @Deprecated
    default Float update(Short key, @NotNull Function<? super Float, ? extends Float> updateFunction) {
        return this.update(key != null ? key.shortValue() : (short) 0, (float l_t) -> {
            final Float result;
            return (result = updateFunction.apply(l_t)) != null ? result : (float) 0;
        });
    }

    float remapping(short key, @NotNull ShortFloatBiFunction<? extends Short> remappingFunction, FloatFloatBiFunction<? extends Float> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(short, ShortFloatBiFunction, FloatFloatBiFunction)
     */
    @Deprecated
    default Float remapping(Short key, @NotNull BiFunction<? super Short, ? super Float, ? extends Short> remappingFunction, @NotNull BiFunction<? super Float, ? super Float, ? extends Float> mergeFunction) {
        return this.remapping(key != null ? key.shortValue() : (short) 0, (short l_t, float l_u) -> remappingFunction.apply(l_t, l_u), (float l_t, float l_u) -> mergeFunction.apply(l_t, l_u));
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

    void forEach(@NotNull ShortFloatBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(ShortFloatBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Short, ? super Float> action) {
        this.forEach((short l_t, float l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull ShortFloatBiOperator function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(ShortFloatBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Short, ? super Float, ? extends Float> function) {
        this.replaceAll((short l_t, float l_u) -> function.apply(l_t, l_u));
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
    ShortSet keySet();

    @NotNull
    FloatCollection values();

    @NotNull
    EntrySetExt<ShortFloatMap.Entry> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<MapExt.Entry<Short, Float>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface Entry extends MapExt.Entry<Short, Float> {
        short getKeyRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getKeyRaw()
         */
        @Deprecated
        default Short getKey() {
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
