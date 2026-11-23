package io.github.shiozakanaptr.acext.api.collection.inttype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.longtype.LongCollection;
import io.github.shiozakanaptr.acext.util.function.inttype.*;
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
public interface IntLongMap extends MapExt<Integer, Long> {
    long get(int key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #get(int)
     */
    @Deprecated
    default Long get(Object key) {
        if (key != null && !(key instanceof Integer)) return this.getDefaultReturnValueRaw();
        return this.get(key != null ? ((Integer) key).intValue() : (int) 0);
    }

    long getOrDefault(int key, long defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(int, long)
     */
    @Deprecated
    default Long getOrDefault(Object key, Long defaultValue) {
        if (key != null && !(key instanceof Integer)) return defaultValue;
        return this.getOrDefault(key != null ? ((Integer) key).intValue() : (int) 0, defaultValue != null ? defaultValue.longValue() : (long) 0);
    }

    long put(int key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(int, long)
     */
    @Deprecated
    default Long put(Integer key, Long value) {
        return this.put(key != null ? key.intValue() : (int) 0, value != null ? value.longValue() : (long) 0);
    }

    long putIfAbsent(int key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(int, long)
     */
    @Deprecated
    default Long putIfAbsent(Integer key, Long value) {
        return this.putIfAbsent(key != null ? key.intValue() : (int) 0, value != null ? value.longValue() : (long) 0);
    }

    long remove(int key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(int)
     */
    @Deprecated
    default Long remove(Object key) {
        if (key != null && !(key instanceof Integer)) return this.getDefaultReturnValueRaw();
        return this.remove(key != null ? ((Integer) key).intValue() : (int) 0);
    }

    boolean remove(int key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(int, long)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Integer)) return false;
        if (value != null && !(value instanceof Long)) return false;
        return this.remove(key != null ? ((Integer) key).intValue() : (int) 0, value != null ? ((Long) value).longValue() : (long) 0);
    }

    long replace(int key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(int, long)
     */
    @Deprecated
    default Long replace(Integer key, Long value) {
        return this.replace(key != null ? key.intValue() : (int) 0, value != null ? value.longValue() : (long) 0);
    }

    boolean replace(int key, long oldValue, long newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(int, long, long)
     */
    @Deprecated
    default boolean replace(Integer key, Long oldValue, Long newValue) {
        return this.replace(key != null ? key.intValue() : (int) 0, oldValue != null ? oldValue.longValue() : (long) 0, newValue != null ? newValue.longValue() : (long) 0);
    }

    long computeIfAbsent(int key, @NotNull IntFunction<? extends Long> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(int, IntFunction)
     */
    @Deprecated
    default Long computeIfAbsent(Integer key, @NotNull Function<? super Integer, ? extends Long> mappingFunction) {
        return this.computeIfAbsent(key != null ? key.intValue() : (int) 0, (Integer l_t) -> mappingFunction.apply(l_t));
    }

    long computeIfPresent(int key, @NotNull IntLongBiFunction<? extends Long> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(int, IntLongBiFunction)
     */
    @Deprecated
    default Long computeIfPresent(Integer key, @NotNull BiFunction<? super Integer, ? super Long, ? extends Long> remappingFunction) {
        return this.computeIfPresent(key != null ? key.intValue() : (int) 0, (int l_t, long l_u) -> remappingFunction.apply(l_t, l_u));
    }

    long compute(int key, @NotNull IntLongBiFunction<? extends Long> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(int, IntLongBiFunction)
     */
    @Deprecated
    default Long compute(Integer key, @NotNull BiFunction<? super Integer, ? super Long, ? extends Long> remappingFunction) {
        return this.compute(key != null ? key.intValue() : (int) 0, (int l_t, long l_u) -> remappingFunction.apply(l_t, l_u));
    }

    long merge(int key, long value, @NotNull LongLongBiFunction<? extends Long> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(int, long, LongLongBiFunction)
     */
    @Deprecated
    default Long merge(Integer key, @NotNull Long value, @NotNull BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
        return this.merge(key != null ? key.intValue() : (int) 0, value.longValue(), (long l_t, long l_u) -> remappingFunction.apply(l_t, l_u));
    }

    long update(int key, @NotNull LongUnaryOperator updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(int, LongUnaryOperator)
     */
    @Deprecated
    default Long update(Integer key, @NotNull Function<? super Long, ? extends Long> updateFunction) {
        return this.update(key != null ? key.intValue() : (int) 0, (long l_t) -> {
            final Long result;
            return (result = updateFunction.apply(l_t)) != null ? result : (long) 0;
        });
    }

    long remapping(int key, @NotNull IntLongBiFunction<? extends Integer> remappingFunction, LongLongBiFunction<? extends Long> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(int, IntLongBiFunction, LongLongBiFunction)
     */
    @Deprecated
    default Long remapping(Integer key, @NotNull BiFunction<? super Integer, ? super Long, ? extends Integer> remappingFunction, @NotNull BiFunction<? super Long, ? super Long, ? extends Long> mergeFunction) {
        return this.remapping(key != null ? key.intValue() : (int) 0, (int l_t, long l_u) -> remappingFunction.apply(l_t, l_u), (long l_t, long l_u) -> mergeFunction.apply(l_t, l_u));
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

    void forEach(@NotNull IntLongBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(IntLongBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Integer, ? super Long> action) {
        this.forEach((int l_t, long l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull IntLongBiOperator function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(IntLongBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Integer, ? super Long, ? extends Long> function) {
        this.replaceAll((int l_t, long l_u) -> function.apply(l_t, l_u));
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
    IntSet keySet();

    @NotNull
    LongCollection values();

    @NotNull
    EntrySetExt<IntLongMap.Entry> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<MapExt.Entry<Integer, Long>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface Entry extends MapExt.Entry<Integer, Long> {
        int getKeyRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getKeyRaw()
         */
        @Deprecated
        default Integer getKey() {
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
