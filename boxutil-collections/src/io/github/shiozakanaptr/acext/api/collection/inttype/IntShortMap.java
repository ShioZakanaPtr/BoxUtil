package io.github.shiozakanaptr.acext.api.collection.inttype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.inttype.IntSet;
import io.github.shiozakanaptr.acext.api.collection.shorttype.ShortCollection;
import io.github.shiozakanaptr.acext.util.function.inttype.*;
import io.github.shiozakanaptr.acext.util.function.shorttype.*;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.function.BiConsumer;

public interface IntShortMap extends MapExt<Integer, Short> {
    short get(int key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #get(int)
     */
    @Deprecated
    default Short get(Object key) {
        if (key != null && !(key instanceof Integer)) return this.getDefaultReturnValueRaw();
        return this.get(key != null ? ((Integer) key).intValue() : (int) 0);
    }

    short getOrDefault(int key, short defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(int, short)
     */
    @Deprecated
    default Short getOrDefault(Object key, Short defaultValue) {
        if (key != null && !(key instanceof Integer)) return defaultValue;
        return this.getOrDefault(key != null ? ((Integer) key).intValue() : (int) 0, defaultValue != null ? defaultValue.shortValue() : (short) 0);
    }

    short put(int key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(int, short)
     */
    @Deprecated
    default Short put(Integer key, Short value) {
        return this.put(key != null ? key.intValue() : (int) 0, value != null ? value.shortValue() : (short) 0);
    }

    short putIfAbsent(int key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(int, short)
     */
    @Deprecated
    default Short putIfAbsent(Integer key, Short value) {
        return this.putIfAbsent(key != null ? key.intValue() : (int) 0, value != null ? value.shortValue() : (short) 0);
    }

    short remove(int key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(int)
     */
    @Deprecated
    default Short remove(Object key) {
        if (key != null && !(key instanceof Integer)) return this.getDefaultReturnValueRaw();
        return this.remove(key != null ? ((Integer) key).intValue() : (int) 0);
    }

    boolean remove(int key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(int, short)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Integer)) return false;
        if (value != null && !(value instanceof Short)) return false;
        return this.remove(key != null ? ((Integer) key).intValue() : (int) 0, value != null ? ((Short) value).shortValue() : (short) 0);
    }

    short replace(int key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(int, short)
     */
    @Deprecated
    default Short replace(Integer key, Short value) {
        return this.replace(key != null ? key.intValue() : (int) 0, value != null ? value.shortValue() : (short) 0);
    }

    boolean replace(int key, short oldValue, short newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(int, short, short)
     */
    @Deprecated
    default boolean replace(Integer key, Short oldValue, Short newValue) {
        return this.replace(key != null ? key.intValue() : (int) 0, oldValue != null ? oldValue.shortValue() : (short) 0, newValue != null ? newValue.shortValue() : (short) 0);
    }

    short computeIfAbsent(int key, @NotNull IntFunction<? extends Short> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(int, IntFunction)
     */
    @Deprecated
    default Short computeIfAbsent(Integer key, @NotNull Function<? super Integer, ? extends Short> mappingFunction) {
        return this.computeIfAbsent(key != null ? key.intValue() : (int) 0, (Integer l_t) -> mappingFunction.apply(l_t));
    }

    short computeIfPresent(int key, @NotNull IntShortBiFunction<? extends Short> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(int, IntShortBiFunction)
     */
    @Deprecated
    default Short computeIfPresent(Integer key, @NotNull BiFunction<? super Integer, ? super Short, ? extends Short> remappingFunction) {
        return this.computeIfPresent(key != null ? key.intValue() : (int) 0, (int l_t, short l_u) -> remappingFunction.apply(l_t, l_u));
    }

    short compute(int key, @NotNull IntShortBiFunction<? extends Short> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(int, IntShortBiFunction)
     */
    @Deprecated
    default Short compute(Integer key, @NotNull BiFunction<? super Integer, ? super Short, ? extends Short> remappingFunction) {
        return this.compute(key != null ? key.intValue() : (int) 0, (int l_t, short l_u) -> remappingFunction.apply(l_t, l_u));
    }

    short merge(int key, short value, @NotNull ShortShortBiFunction<? extends Short> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(int, short, ShortShortBiFunction)
     */
    @Deprecated
    default Short merge(Integer key, @NotNull Short value, @NotNull BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
        return this.merge(key != null ? key.intValue() : (int) 0, value != null ? value.shortValue() : (short) 0, (short l_t, short l_u) -> remappingFunction.apply(l_t, l_u));
    }

    short update(int key, @NotNull ShortUnaryOperator updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(int, ShortUnaryOperator)
     */
    @Deprecated
    default Short update(Integer key, @NotNull Function<? super Short, ? extends Short> updateFunction) {
        return this.update(key != null ? key.intValue() : (int) 0, (short l_t) -> {
            final Short result;
            return (result = updateFunction.apply(l_t)) != null ? result : (short) 0;
        });
    }

    short remapping(int key, @NotNull IntShortBiFunction<? extends Integer> remappingFunction, ShortShortBiFunction<? extends Short> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(int, IntShortBiFunction, ShortShortBiFunction)
     */
    @Deprecated
    default Short remapping(Integer key, @NotNull BiFunction<? super Integer, ? super Short, ? extends Integer> remappingFunction, @NotNull BiFunction<? super Short, ? super Short, ? extends Short> mergeFunction) {
        return this.remapping(key != null ? key.intValue() : (int) 0, (int l_t, short l_u) -> remappingFunction.apply(l_t, l_u), (short l_t, short l_u) -> mergeFunction.apply(l_t, l_u));
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

    boolean containsValue(short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #containsValue(short)
     */
    @Deprecated
    default boolean containsValue(Object value) {
        if (value != null && !(value instanceof Short)) return false;
        return this.containsValue(value != null ? ((Short) value).shortValue() : (short) 0);
    }

    void forEach(@NotNull IntShortBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(IntShortBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Integer, ? super Short> action) {
        this.forEach((int l_t, short l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull IntShortBiOperator function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(IntShortBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Integer, ? super Short, ? extends Short> function) {
        this.replaceAll((int l_t, short l_u) -> function.apply(l_t, l_u));
    }

    short getDefaultReturnValueRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getDefaultReturnValueRaw()
     */
    @Deprecated
    default Short getDefaultReturnValue() {
        return this.getDefaultReturnValueRaw();
    }

    short setDefaultReturnValue(short newDefault);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #setDefaultReturnValue(short)
     */
    @Deprecated
    default Short setDefaultReturnValue(Short newDefault) {
        return this.setDefaultReturnValue(newDefault != null ? newDefault.shortValue() : (short) 0);
    }

    @NotNull
    IntSet keySet();

    @NotNull
    ShortCollection values();

    @NotNull
    EntrySetExt<IntShortMap.Entry> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<MapExt.Entry<Integer, Short>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface Entry extends MapExt.Entry<Integer, Short> {
        int getKeyRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getKeyRaw()
         */
        @Deprecated
        default Integer getKey() {
            return this.getKeyRaw();
        }

        short getValueRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getValueRaw()
         */
        @Deprecated
        default Short getValue() {
            return this.getValueRaw();
        }

        short setValue(short newValue);

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #setValue(short)
         */
        @Deprecated
        default Short setValue(Short value) {
            return this.setValue(value != null ? value.shortValue() : (short) 0);
        }
    }

}
