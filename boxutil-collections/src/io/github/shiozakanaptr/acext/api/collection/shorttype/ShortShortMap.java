package io.github.shiozakanaptr.acext.api.collection.shorttype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
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
public interface ShortShortMap extends MapExt<Short, Short> {
    short get(short key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #get(short)
     */
    @Deprecated
    default Short get(Object key) {
        if (key != null && !(key instanceof Short)) return this.getDefaultReturnValueRaw();
        return this.get(key != null ? ((Short) key).shortValue() : (short) 0);
    }

    short getOrDefault(short key, short defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(short, short)
     */
    @Deprecated
    default Short getOrDefault(Object key, Short defaultValue) {
        if (key != null && !(key instanceof Short)) return defaultValue;
        return this.getOrDefault(key != null ? ((Short) key).shortValue() : (short) 0, defaultValue != null ? defaultValue.shortValue() : (short) 0);
    }

    short put(short key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(short, short)
     */
    @Deprecated
    default Short put(Short key, Short value) {
        return this.put(key != null ? key.shortValue() : (short) 0, value != null ? value.shortValue() : (short) 0);
    }

    short putIfAbsent(short key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(short, short)
     */
    @Deprecated
    default Short putIfAbsent(Short key, Short value) {
        return this.putIfAbsent(key != null ? key.shortValue() : (short) 0, value != null ? value.shortValue() : (short) 0);
    }

    short remove(short key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(short)
     */
    @Deprecated
    default Short remove(Object key) {
        if (key != null && !(key instanceof Short)) return this.getDefaultReturnValueRaw();
        return this.remove(key != null ? ((Short) key).shortValue() : (short) 0);
    }

    boolean remove(short key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(short, short)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Short)) return false;
        if (value != null && !(value instanceof Short)) return false;
        return this.remove(key != null ? ((Short) key).shortValue() : (short) 0, value != null ? ((Short) value).shortValue() : (short) 0);
    }

    short replace(short key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(short, short)
     */
    @Deprecated
    default Short replace(Short key, Short value) {
        return this.replace(key != null ? key.shortValue() : (short) 0, value != null ? value.shortValue() : (short) 0);
    }

    boolean replace(short key, short oldValue, short newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(short, short, short)
     */
    @Deprecated
    default boolean replace(Short key, Short oldValue, Short newValue) {
        return this.replace(key != null ? key.shortValue() : (short) 0, oldValue != null ? oldValue.shortValue() : (short) 0, newValue != null ? newValue.shortValue() : (short) 0);
    }

    short computeIfAbsent(short key, @NotNull ShortFunction<? extends Short> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(short, ShortFunction)
     */
    @Deprecated
    default Short computeIfAbsent(Short key, @NotNull Function<? super Short, ? extends Short> mappingFunction) {
        return this.computeIfAbsent(key != null ? key.shortValue() : (short) 0, (Short l_t) -> mappingFunction.apply(l_t));
    }

    short computeIfPresent(short key, @NotNull ShortShortBiFunction<? extends Short> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(short, ShortShortBiFunction)
     */
    @Deprecated
    default Short computeIfPresent(Short key, @NotNull BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
        return this.computeIfPresent(key != null ? key.shortValue() : (short) 0, (short l_t, short l_u) -> remappingFunction.apply(l_t, l_u));
    }

    short compute(short key, @NotNull ShortShortBiFunction<? extends Short> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(short, ShortShortBiFunction)
     */
    @Deprecated
    default Short compute(Short key, @NotNull BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
        return this.compute(key != null ? key.shortValue() : (short) 0, (short l_t, short l_u) -> remappingFunction.apply(l_t, l_u));
    }

    short merge(short key, short value, @NotNull ShortShortBiFunction<? extends Short> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(short, short, ShortShortBiFunction)
     */
    @Deprecated
    default Short merge(Short key, @NotNull Short value, @NotNull BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
        return this.merge(key != null ? key.shortValue() : (short) 0, value != null ? value.shortValue() : (short) 0, (short l_t, short l_u) -> remappingFunction.apply(l_t, l_u));
    }

    short update(short key, @NotNull ShortUnaryOperator updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(short, ShortUnaryOperator)
     */
    @Deprecated
    default Short update(Short key, @NotNull Function<? super Short, ? extends Short> updateFunction) {
        return this.update(key != null ? key.shortValue() : (short) 0, (short l_t) -> {
            final Short result;
            return (result = updateFunction.apply(l_t)) != null ? result : (short) 0;
        });
    }

    short remapping(short key, @NotNull ShortShortBiFunction<? extends Short> remappingFunction, ShortShortBiFunction<? extends Short> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(short, ShortShortBiFunction, ShortShortBiFunction)
     */
    @Deprecated
    default Short remapping(Short key, @NotNull BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction, @NotNull BiFunction<? super Short, ? super Short, ? extends Short> mergeFunction) {
        return this.remapping(key != null ? key.shortValue() : (short) 0, (short l_t, short l_u) -> remappingFunction.apply(l_t, l_u), (short l_t, short l_u) -> mergeFunction.apply(l_t, l_u));
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

    void forEach(@NotNull ShortShortBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(ShortShortBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Short, ? super Short> action) {
        this.forEach((short l_t, short l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull ShortShortBiOperator function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(ShortShortBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Short, ? super Short, ? extends Short> function) {
        this.replaceAll((short l_t, short l_u) -> function.apply(l_t, l_u));
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
    ShortSet keySet();

    @NotNull
    ShortCollection values();

    @NotNull
    EntrySetExt<ShortShortMap.Entry> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<MapExt.Entry<Short, Short>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface Entry extends MapExt.Entry<Short, Short> {
        short getKeyRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getKeyRaw()
         */
        @Deprecated
        default Short getKey() {
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
