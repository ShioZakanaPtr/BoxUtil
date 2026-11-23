package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.shorttype.ShortCollection;
import io.github.shiozakanaptr.acext.util.function.objecttype.*;
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
public interface ObjShortMap<K> extends MapExt<K, Short> {
    short getRaw(Object key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getRaw(Object)
     */
    @Deprecated
    default Short get(Object key) {
        return this.getRaw(key);
    }

    short getOrDefault(Object key, short defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(Object, short)
     */
    @Deprecated
    default Short getOrDefault(Object key, Short defaultValue) {
        return this.getOrDefault(key, defaultValue != null ? defaultValue.shortValue() : (short) 0);
    }

    short put(K key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(K, short)
     */
    @Deprecated
    default Short put(K key, Short value) {
        return this.put(key, value != null ? value.shortValue() : (short) 0);
    }

    short putIfAbsent(K key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(K, short)
     */
    @Deprecated
    default Short putIfAbsent(K key, Short value) {
        return this.putIfAbsent(key, value != null ? value.shortValue() : (short) 0);
    }

    short removeRaw(Object key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeRaw(Object)
     */
    @Deprecated
    default Short remove(Object key) {
        return this.removeRaw(key);
    }

    boolean remove(Object key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(Object, short)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (value != null && !(value instanceof Short)) return false;
        return this.remove(key, value != null ? ((Short) value).shortValue() : (short) 0);
    }

    short replace(K key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(K, short)
     */
    @Deprecated
    default Short replace(K key, Short value) {
        return this.replace(key, value != null ? value.shortValue() : (short) 0);
    }

    boolean replace(K key, short oldValue, short newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(K, short, short)
     */
    @Deprecated
    default boolean replace(K key, Short oldValue, Short newValue) {
        return this.replace(key, oldValue != null ? oldValue.shortValue() : (short) 0, newValue != null ? newValue.shortValue() : (short) 0);
    }

    short computeIfAbsentRaw(K key, @NotNull Function<? super K, ? extends Short> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsentRaw(K, Function)
     */
    @Deprecated
    default Short computeIfAbsent(K key, @NotNull Function<? super K, ? extends Short> mappingFunction) {
        return this.computeIfAbsentRaw(key, (K l_t) -> mappingFunction.apply(l_t));
    }

    short computeIfPresent(K key, @NotNull ObjShortBiFunction<? super K, ? extends Short> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(K, ObjShortBiFunction)
     */
    @Deprecated
    default Short computeIfPresent(K key, @NotNull BiFunction<? super K, ? super Short, ? extends Short> remappingFunction) {
        return this.computeIfPresent(key, (K l_t, short l_u) -> remappingFunction.apply(l_t, l_u));
    }

    short compute(K key, @NotNull ObjShortBiFunction<? super K, ? extends Short> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(K, ObjShortBiFunction)
     */
    @Deprecated
    default Short compute(K key, @NotNull BiFunction<? super K, ? super Short, ? extends Short> remappingFunction) {
        return this.compute(key, (K l_t, short l_u) -> remappingFunction.apply(l_t, l_u));
    }

    short merge(K key, short value, @NotNull ShortShortBiFunction<? extends Short> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(K, short, ShortShortBiFunction)
     */
    @Deprecated
    default Short merge(K key, @NotNull Short value, @NotNull BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
        return this.merge(key, value != null ? value.shortValue() : (short) 0, (short l_t, short l_u) -> remappingFunction.apply(l_t, l_u));
    }

    short update(K key, @NotNull ShortUnaryOperator updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(K, ShortUnaryOperator)
     */
    @Deprecated
    default Short update(K key, @NotNull Function<? super Short, ? extends Short> updateFunction) {
        return this.update(key, (short l_t) -> {
            final Short result;
            return (result = updateFunction.apply(l_t)) != null ? result : (short) 0;
        });
    }

    short remapping(K key, @NotNull ObjShortBiFunction<? super K, ? extends K> remappingFunction, ShortShortBiFunction<? extends Short> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(K, ObjShortBiFunction, ShortShortBiFunction)
     */
    @Deprecated
    default Short remapping(K key, @NotNull BiFunction<? super K, ? super Short, ? extends K> remappingFunction, @NotNull BiFunction<? super Short, ? super Short, ? extends Short> mergeFunction) {
        return this.remapping(key, (K l_t, short l_u) -> remappingFunction.apply(l_t, l_u), (short l_t, short l_u) -> mergeFunction.apply(l_t, l_u));
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

    void forEach(@NotNull ObjShortBiConsumer<? super K> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(ObjShortBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super K, ? super Short> action) {
        this.forEach((K l_t, short l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull ObjShortBiOperator<? super K> function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(ObjShortBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super K, ? super Short, ? extends Short> function) {
        this.replaceAll((K l_t, short l_u) -> function.apply(l_t, l_u));
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
    ObjSet<K> keySet();

    @NotNull
    ShortCollection values();

    @NotNull
    EntrySetExt<ObjShortMap.Entry<K>> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<MapExt.Entry<K, Short>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface Entry<K> extends MapExt.Entry<K, Short> {

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
