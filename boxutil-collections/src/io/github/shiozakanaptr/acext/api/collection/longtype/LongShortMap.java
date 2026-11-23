package io.github.shiozakanaptr.acext.api.collection.longtype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.shorttype.ShortCollection;
import io.github.shiozakanaptr.acext.util.function.longtype.*;
import io.github.shiozakanaptr.acext.util.function.shorttype.*;
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
public interface LongShortMap extends MapExt<Long, Short> {
    short get(long key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #get(long)
     */
    @Deprecated
    default Short get(Object key) {
        if (key != null && !(key instanceof Long)) return this.getDefaultReturnValueRaw();
        return this.get(key != null ? ((Long) key).longValue() : (long) 0);
    }

    short getOrDefault(long key, short defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(long, short)
     */
    @Deprecated
    default Short getOrDefault(Object key, Short defaultValue) {
        if (key != null && !(key instanceof Long)) return defaultValue;
        return this.getOrDefault(key != null ? ((Long) key).longValue() : (long) 0, defaultValue != null ? defaultValue.shortValue() : (short) 0);
    }

    short put(long key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(long, short)
     */
    @Deprecated
    default Short put(Long key, Short value) {
        return this.put(key != null ? key.longValue() : (long) 0, value != null ? value.shortValue() : (short) 0);
    }

    short putIfAbsent(long key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(long, short)
     */
    @Deprecated
    default Short putIfAbsent(Long key, Short value) {
        return this.putIfAbsent(key != null ? key.longValue() : (long) 0, value != null ? value.shortValue() : (short) 0);
    }

    short remove(long key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(long)
     */
    @Deprecated
    default Short remove(Object key) {
        if (key != null && !(key instanceof Long)) return this.getDefaultReturnValueRaw();
        return this.remove(key != null ? ((Long) key).longValue() : (long) 0);
    }

    boolean remove(long key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(long, short)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Long)) return false;
        if (value != null && !(value instanceof Short)) return false;
        return this.remove(key != null ? ((Long) key).longValue() : (long) 0, value != null ? ((Short) value).shortValue() : (short) 0);
    }

    short replace(long key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(long, short)
     */
    @Deprecated
    default Short replace(Long key, Short value) {
        return this.replace(key != null ? key.longValue() : (long) 0, value != null ? value.shortValue() : (short) 0);
    }

    boolean replace(long key, short oldValue, short newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(long, short, short)
     */
    @Deprecated
    default boolean replace(Long key, Short oldValue, Short newValue) {
        return this.replace(key != null ? key.longValue() : (long) 0, oldValue != null ? oldValue.shortValue() : (short) 0, newValue != null ? newValue.shortValue() : (short) 0);
    }

    short computeIfAbsent(long key, @NotNull LongFunction<? extends Short> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(long, LongFunction)
     */
    @Deprecated
    default Short computeIfAbsent(Long key, @NotNull Function<? super Long, ? extends Short> mappingFunction) {
        return this.computeIfAbsent(key != null ? key.longValue() : (long) 0, (Long l_t) -> mappingFunction.apply(l_t));
    }

    short computeIfPresent(long key, @NotNull LongShortBiFunction<? extends Short> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(long, LongShortBiFunction)
     */
    @Deprecated
    default Short computeIfPresent(Long key, @NotNull BiFunction<? super Long, ? super Short, ? extends Short> remappingFunction) {
        return this.computeIfPresent(key != null ? key.longValue() : (long) 0, (long l_t, short l_u) -> remappingFunction.apply(l_t, l_u));
    }

    short compute(long key, @NotNull LongShortBiFunction<? extends Short> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(long, LongShortBiFunction)
     */
    @Deprecated
    default Short compute(Long key, @NotNull BiFunction<? super Long, ? super Short, ? extends Short> remappingFunction) {
        return this.compute(key != null ? key.longValue() : (long) 0, (long l_t, short l_u) -> remappingFunction.apply(l_t, l_u));
    }

    short merge(long key, short value, @NotNull ShortShortBiFunction<? extends Short> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(long, short, ShortShortBiFunction)
     */
    @Deprecated
    default Short merge(Long key, @NotNull Short value, @NotNull BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
        return this.merge(key != null ? key.longValue() : (long) 0, value.shortValue(), (short l_t, short l_u) -> remappingFunction.apply(l_t, l_u));
    }

    short update(long key, @NotNull ShortUnaryOperator updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(long, ShortUnaryOperator)
     */
    @Deprecated
    default Short update(Long key, @NotNull Function<? super Short, ? extends Short> updateFunction) {
        return this.update(key != null ? key.longValue() : (long) 0, (short l_t) -> {
            final Short result;
            return (result = updateFunction.apply(l_t)) != null ? result : (short) 0;
        });
    }

    short remapping(long key, @NotNull LongShortBiFunction<? extends Long> remappingFunction, ShortShortBiFunction<? extends Short> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(long, LongShortBiFunction, ShortShortBiFunction)
     */
    @Deprecated
    default Short remapping(Long key, @NotNull BiFunction<? super Long, ? super Short, ? extends Long> remappingFunction, @NotNull BiFunction<? super Short, ? super Short, ? extends Short> mergeFunction) {
        return this.remapping(key != null ? key.longValue() : (long) 0, (long l_t, short l_u) -> remappingFunction.apply(l_t, l_u), (short l_t, short l_u) -> mergeFunction.apply(l_t, l_u));
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

    void forEach(@NotNull LongShortBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(LongShortBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Long, ? super Short> action) {
        this.forEach((long l_t, short l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull LongShortBiOperator function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(LongShortBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Long, ? super Short, ? extends Short> function) {
        this.replaceAll((long l_t, short l_u) -> function.apply(l_t, l_u));
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
    LongSet keySet();

    @NotNull
    ShortCollection values();

    @NotNull
    EntrySetExt<LongShortMap.Entry> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<MapExt.Entry<Long, Short>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface Entry extends MapExt.Entry<Long, Short> {
        long getKeyRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getKeyRaw()
         */
        @Deprecated
        default Long getKey() {
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
