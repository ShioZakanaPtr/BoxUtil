package io.github.shiozakanaptr.acext.api.collection.floattype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.floattype.FloatSet;
import io.github.shiozakanaptr.acext.api.collection.inttype.IntCollection;
import io.github.shiozakanaptr.acext.util.function.floattype.*;
import io.github.shiozakanaptr.acext.util.function.inttype.*;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.function.BiConsumer;

public interface FloatIntMap extends MapExt<Float, Integer> {
    int get(float key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #get(float)
     */
    @Deprecated
    default Integer get(Object key) {
        if (key != null && !(key instanceof Float)) return this.getDefaultReturnValueRaw();
        return this.get(key != null ? ((Float) key).floatValue() : (float) 0);
    }

    int getOrDefault(float key, int defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(float, int)
     */
    @Deprecated
    default Integer getOrDefault(Object key, Integer defaultValue) {
        if (key != null && !(key instanceof Float)) return defaultValue;
        return this.getOrDefault(key != null ? ((Float) key).floatValue() : (float) 0, defaultValue != null ? defaultValue.intValue() : (int) 0);
    }

    int put(float key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(float, int)
     */
    @Deprecated
    default Integer put(Float key, Integer value) {
        return this.put(key != null ? key.floatValue() : (float) 0, value != null ? value.intValue() : (int) 0);
    }

    int putIfAbsent(float key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(float, int)
     */
    @Deprecated
    default Integer putIfAbsent(Float key, Integer value) {
        return this.putIfAbsent(key != null ? key.floatValue() : (float) 0, value != null ? value.intValue() : (int) 0);
    }

    int remove(float key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(float)
     */
    @Deprecated
    default Integer remove(Object key) {
        if (key != null && !(key instanceof Float)) return this.getDefaultReturnValueRaw();
        return this.remove(key != null ? ((Float) key).floatValue() : (float) 0);
    }

    boolean remove(float key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(float, int)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Float)) return false;
        if (value != null && !(value instanceof Integer)) return false;
        return this.remove(key != null ? ((Float) key).floatValue() : (float) 0, value != null ? ((Integer) value).intValue() : (int) 0);
    }

    int replace(float key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(float, int)
     */
    @Deprecated
    default Integer replace(Float key, Integer value) {
        return this.replace(key != null ? key.floatValue() : (float) 0, value != null ? value.intValue() : (int) 0);
    }

    boolean replace(float key, int oldValue, int newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(float, int, int)
     */
    @Deprecated
    default boolean replace(Float key, Integer oldValue, Integer newValue) {
        return this.replace(key != null ? key.floatValue() : (float) 0, oldValue != null ? oldValue.intValue() : (int) 0, newValue != null ? newValue.intValue() : (int) 0);
    }

    int computeIfAbsent(float key, @NotNull FloatFunction<? extends Integer> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(float, FloatFunction)
     */
    @Deprecated
    default Integer computeIfAbsent(Float key, @NotNull Function<? super Float, ? extends Integer> mappingFunction) {
        return this.computeIfAbsent(key != null ? key.floatValue() : (float) 0, (Float l_t) -> mappingFunction.apply(l_t));
    }

    int computeIfPresent(float key, @NotNull FloatIntBiFunction<? extends Integer> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(float, FloatIntBiFunction)
     */
    @Deprecated
    default Integer computeIfPresent(Float key, @NotNull BiFunction<? super Float, ? super Integer, ? extends Integer> remappingFunction) {
        return this.computeIfPresent(key != null ? key.floatValue() : (float) 0, (float l_t, int l_u) -> remappingFunction.apply(l_t, l_u));
    }

    int compute(float key, @NotNull FloatIntBiFunction<? extends Integer> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(float, FloatIntBiFunction)
     */
    @Deprecated
    default Integer compute(Float key, @NotNull BiFunction<? super Float, ? super Integer, ? extends Integer> remappingFunction) {
        return this.compute(key != null ? key.floatValue() : (float) 0, (float l_t, int l_u) -> remappingFunction.apply(l_t, l_u));
    }

    int merge(float key, int value, @NotNull IntIntBiFunction<? extends Integer> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(float, int, IntIntBiFunction)
     */
    @Deprecated
    default Integer merge(Float key, @NotNull Integer value, @NotNull BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        return this.merge(key != null ? key.floatValue() : (float) 0, value != null ? value.intValue() : (int) 0, (int l_t, int l_u) -> remappingFunction.apply(l_t, l_u));
    }

    int update(float key, @NotNull IntUnaryOperator updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(float, IntUnaryOperator)
     */
    @Deprecated
    default Integer update(Float key, @NotNull Function<? super Integer, ? extends Integer> updateFunction) {
        return this.update(key != null ? key.floatValue() : (float) 0, (int l_t) -> {
            final Integer result;
            return (result = updateFunction.apply(l_t)) != null ? result : (int) 0;
        });
    }

    int remapping(float key, @NotNull FloatIntBiFunction<? extends Float> remappingFunction, IntIntBiFunction<? extends Integer> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(float, FloatIntBiFunction, IntIntBiFunction)
     */
    @Deprecated
    default Integer remapping(Float key, @NotNull BiFunction<? super Float, ? super Integer, ? extends Float> remappingFunction, @NotNull BiFunction<? super Integer, ? super Integer, ? extends Integer> mergeFunction) {
        return this.remapping(key != null ? key.floatValue() : (float) 0, (float l_t, int l_u) -> remappingFunction.apply(l_t, l_u), (int l_t, int l_u) -> mergeFunction.apply(l_t, l_u));
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

    void forEach(@NotNull FloatIntBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(FloatIntBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Float, ? super Integer> action) {
        this.forEach((float l_t, int l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull FloatIntBiOperator function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(FloatIntBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Float, ? super Integer, ? extends Integer> function) {
        this.replaceAll((float l_t, int l_u) -> function.apply(l_t, l_u));
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
    FloatSet keySet();

    @NotNull
    IntCollection values();

    @NotNull
    EntrySetExt<FloatIntMap.Entry> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<MapExt.Entry<Float, Integer>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface Entry extends MapExt.Entry<Float, Integer> {
        float getKeyRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getKeyRaw()
         */
        @Deprecated
        default Float getKey() {
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
