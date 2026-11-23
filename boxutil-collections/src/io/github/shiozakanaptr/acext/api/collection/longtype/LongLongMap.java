package io.github.shiozakanaptr.acext.api.collection.longtype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
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
public interface LongLongMap extends MapExt<Long, Long> {
    long get(long key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #get(long)
     */
    @Deprecated
    default Long get(Object key) {
        if (key != null && !(key instanceof Long)) return this.getDefaultReturnValueRaw();
        return this.get(key != null ? ((Long) key).longValue() : (long) 0);
    }

    long getOrDefault(long key, long defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(long, long)
     */
    @Deprecated
    default Long getOrDefault(Object key, Long defaultValue) {
        if (key != null && !(key instanceof Long)) return defaultValue;
        return this.getOrDefault(key != null ? ((Long) key).longValue() : (long) 0, defaultValue != null ? defaultValue.longValue() : (long) 0);
    }

    long put(long key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(long, long)
     */
    @Deprecated
    default Long put(Long key, Long value) {
        return this.put(key != null ? key.longValue() : (long) 0, value != null ? value.longValue() : (long) 0);
    }

    long putIfAbsent(long key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(long, long)
     */
    @Deprecated
    default Long putIfAbsent(Long key, Long value) {
        return this.putIfAbsent(key != null ? key.longValue() : (long) 0, value != null ? value.longValue() : (long) 0);
    }

    long remove(long key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(long)
     */
    @Deprecated
    default Long remove(Object key) {
        if (key != null && !(key instanceof Long)) return this.getDefaultReturnValueRaw();
        return this.remove(key != null ? ((Long) key).longValue() : (long) 0);
    }

    boolean remove(long key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(long, long)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Long)) return false;
        if (value != null && !(value instanceof Long)) return false;
        return this.remove(key != null ? ((Long) key).longValue() : (long) 0, value != null ? ((Long) value).longValue() : (long) 0);
    }

    long replace(long key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(long, long)
     */
    @Deprecated
    default Long replace(Long key, Long value) {
        return this.replace(key != null ? key.longValue() : (long) 0, value != null ? value.longValue() : (long) 0);
    }

    boolean replace(long key, long oldValue, long newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(long, long, long)
     */
    @Deprecated
    default boolean replace(Long key, Long oldValue, Long newValue) {
        return this.replace(key != null ? key.longValue() : (long) 0, oldValue != null ? oldValue.longValue() : (long) 0, newValue != null ? newValue.longValue() : (long) 0);
    }

    long computeIfAbsent(long key, @NotNull LongFunction<? extends Long> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(long, LongFunction)
     */
    @Deprecated
    default Long computeIfAbsent(Long key, @NotNull Function<? super Long, ? extends Long> mappingFunction) {
        return this.computeIfAbsent(key != null ? key.longValue() : (long) 0, (Long l_t) -> mappingFunction.apply(l_t));
    }

    long computeIfPresent(long key, @NotNull LongLongBiFunction<? extends Long> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(long, LongLongBiFunction)
     */
    @Deprecated
    default Long computeIfPresent(Long key, @NotNull BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
        return this.computeIfPresent(key != null ? key.longValue() : (long) 0, (long l_t, long l_u) -> remappingFunction.apply(l_t, l_u));
    }

    long compute(long key, @NotNull LongLongBiFunction<? extends Long> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(long, LongLongBiFunction)
     */
    @Deprecated
    default Long compute(Long key, @NotNull BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
        return this.compute(key != null ? key.longValue() : (long) 0, (long l_t, long l_u) -> remappingFunction.apply(l_t, l_u));
    }

    long merge(long key, long value, @NotNull LongLongBiFunction<? extends Long> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(long, long, LongLongBiFunction)
     */
    @Deprecated
    default Long merge(Long key, @NotNull Long value, @NotNull BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
        return this.merge(key != null ? key.longValue() : (long) 0, value != null ? value.longValue() : (long) 0, (long l_t, long l_u) -> remappingFunction.apply(l_t, l_u));
    }

    long update(long key, @NotNull LongUnaryOperator updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(long, LongUnaryOperator)
     */
    @Deprecated
    default Long update(Long key, @NotNull Function<? super Long, ? extends Long> updateFunction) {
        return this.update(key != null ? key.longValue() : (long) 0, (long l_t) -> {
            final Long result;
            return (result = updateFunction.apply(l_t)) != null ? result : (long) 0;
        });
    }

    long remapping(long key, @NotNull LongLongBiFunction<? extends Long> remappingFunction, LongLongBiFunction<? extends Long> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(long, LongLongBiFunction, LongLongBiFunction)
     */
    @Deprecated
    default Long remapping(Long key, @NotNull BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction, @NotNull BiFunction<? super Long, ? super Long, ? extends Long> mergeFunction) {
        return this.remapping(key != null ? key.longValue() : (long) 0, (long l_t, long l_u) -> remappingFunction.apply(l_t, l_u), (long l_t, long l_u) -> mergeFunction.apply(l_t, l_u));
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

    void forEach(@NotNull LongLongBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(LongLongBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Long, ? super Long> action) {
        this.forEach((long l_t, long l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull LongLongBiOperator function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(LongLongBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Long, ? super Long, ? extends Long> function) {
        this.replaceAll((long l_t, long l_u) -> function.apply(l_t, l_u));
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
    LongSet keySet();

    @NotNull
    LongCollection values();

    @NotNull
    EntrySetExt<LongLongMap.Entry> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<MapExt.Entry<Long, Long>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface Entry extends MapExt.Entry<Long, Long> {
        long getKeyRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getKeyRaw()
         */
        @Deprecated
        default Long getKey() {
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
