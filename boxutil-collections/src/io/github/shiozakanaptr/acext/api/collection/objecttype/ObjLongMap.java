package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.longtype.LongCollection;
import io.github.shiozakanaptr.acext.util.function.objecttype.*;
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
public interface ObjLongMap<K> extends MapExt<K, Long> {
    long getRaw(Object key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getRaw(Object)
     */
    @Deprecated
    default Long get(Object key) {
        return this.getRaw(key);
    }

    long getOrDefault(Object key, long defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(Object, long)
     */
    @Deprecated
    default Long getOrDefault(Object key, Long defaultValue) {
        return this.getOrDefault(key, defaultValue != null ? defaultValue.longValue() : (long) 0);
    }

    long put(K key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(K, long)
     */
    @Deprecated
    default Long put(K key, Long value) {
        return this.put(key, value != null ? value.longValue() : (long) 0);
    }

    long putIfAbsent(K key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(K, long)
     */
    @Deprecated
    default Long putIfAbsent(K key, Long value) {
        return this.putIfAbsent(key, value != null ? value.longValue() : (long) 0);
    }

    long removeRaw(Object key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeRaw(Object)
     */
    @Deprecated
    default Long remove(Object key) {
        return this.removeRaw(key);
    }

    boolean remove(Object key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(Object, long)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (value != null && !(value instanceof Long)) return false;
        return this.remove(key, value != null ? ((Long) value).longValue() : (long) 0);
    }

    long replace(K key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(K, long)
     */
    @Deprecated
    default Long replace(K key, Long value) {
        return this.replace(key, value != null ? value.longValue() : (long) 0);
    }

    boolean replace(K key, long oldValue, long newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(K, long, long)
     */
    @Deprecated
    default boolean replace(K key, Long oldValue, Long newValue) {
        return this.replace(key, oldValue != null ? oldValue.longValue() : (long) 0, newValue != null ? newValue.longValue() : (long) 0);
    }

    long computeIfAbsentRaw(K key, @NotNull Function<? super K, ? extends Long> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsentRaw(K, Function)
     */
    @Deprecated
    default Long computeIfAbsent(K key, @NotNull Function<? super K, ? extends Long> mappingFunction) {
        return this.computeIfAbsentRaw(key, (K l_t) -> mappingFunction.apply(l_t));
    }

    long computeIfPresent(K key, @NotNull ObjLongBiFunction<? super K, ? extends Long> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(K, ObjLongBiFunction)
     */
    @Deprecated
    default Long computeIfPresent(K key, @NotNull BiFunction<? super K, ? super Long, ? extends Long> remappingFunction) {
        return this.computeIfPresent(key, (K l_t, long l_u) -> remappingFunction.apply(l_t, l_u));
    }

    long compute(K key, @NotNull ObjLongBiFunction<? super K, ? extends Long> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(K, ObjLongBiFunction)
     */
    @Deprecated
    default Long compute(K key, @NotNull BiFunction<? super K, ? super Long, ? extends Long> remappingFunction) {
        return this.compute(key, (K l_t, long l_u) -> remappingFunction.apply(l_t, l_u));
    }

    long merge(K key, long value, @NotNull LongLongBiFunction<? extends Long> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(K, long, LongLongBiFunction)
     */
    @Deprecated
    default Long merge(K key, @NotNull Long value, @NotNull BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
        return this.merge(key, value != null ? value.longValue() : (long) 0, (long l_t, long l_u) -> remappingFunction.apply(l_t, l_u));
    }

    long update(K key, @NotNull LongUnaryOperator updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(K, LongUnaryOperator)
     */
    @Deprecated
    default Long update(K key, @NotNull Function<? super Long, ? extends Long> updateFunction) {
        return this.update(key, (long l_t) -> {
            final Long result;
            return (result = updateFunction.apply(l_t)) != null ? result : (long) 0;
        });
    }

    long remapping(K key, @NotNull ObjLongBiFunction<? super K, ? extends K> remappingFunction, LongLongBiFunction<? extends Long> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(K, ObjLongBiFunction, LongLongBiFunction)
     */
    @Deprecated
    default Long remapping(K key, @NotNull BiFunction<? super K, ? super Long, ? extends K> remappingFunction, @NotNull BiFunction<? super Long, ? super Long, ? extends Long> mergeFunction) {
        return this.remapping(key, (K l_t, long l_u) -> remappingFunction.apply(l_t, l_u), (long l_t, long l_u) -> mergeFunction.apply(l_t, l_u));
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

    void forEach(@NotNull ObjLongBiConsumer<? super K> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(ObjLongBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super K, ? super Long> action) {
        this.forEach((K l_t, long l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull ObjLongBiOperator<? super K> function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(ObjLongBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super K, ? super Long, ? extends Long> function) {
        this.replaceAll((K l_t, long l_u) -> function.apply(l_t, l_u));
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
    ObjSet<K> keySet();

    @NotNull
    LongCollection values();

    @NotNull
    EntrySetExt<ObjLongMap.Entry<K>> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<MapExt.Entry<K, Long>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface Entry<K> extends MapExt.Entry<K, Long> {

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
