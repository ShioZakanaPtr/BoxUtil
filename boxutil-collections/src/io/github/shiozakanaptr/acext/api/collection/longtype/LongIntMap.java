package io.github.shiozakanaptr.acext.api.collection.longtype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.inttype.IntCollection;
import io.github.shiozakanaptr.acext.util.function.longtype.*;
import io.github.shiozakanaptr.acext.util.function.inttype.*;
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
public interface LongIntMap extends MapExt<Long, Integer> {
    int get(long key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #get(long)
     */
    @Deprecated
    default Integer get(Object key) {
        if (key != null && !(key instanceof Long)) return this.getDefaultReturnValueRaw();
        return this.get(key != null ? ((Long) key).longValue() : (long) 0);
    }

    int getOrDefault(long key, int defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(long, int)
     */
    @Deprecated
    default Integer getOrDefault(Object key, Integer defaultValue) {
        if (key != null && !(key instanceof Long)) return defaultValue;
        return this.getOrDefault(key != null ? ((Long) key).longValue() : (long) 0, defaultValue != null ? defaultValue.intValue() : (int) 0);
    }

    int put(long key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(long, int)
     */
    @Deprecated
    default Integer put(Long key, Integer value) {
        return this.put(key != null ? key.longValue() : (long) 0, value != null ? value.intValue() : (int) 0);
    }

    int putIfAbsent(long key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(long, int)
     */
    @Deprecated
    default Integer putIfAbsent(Long key, Integer value) {
        return this.putIfAbsent(key != null ? key.longValue() : (long) 0, value != null ? value.intValue() : (int) 0);
    }

    int remove(long key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(long)
     */
    @Deprecated
    default Integer remove(Object key) {
        if (key != null && !(key instanceof Long)) return this.getDefaultReturnValueRaw();
        return this.remove(key != null ? ((Long) key).longValue() : (long) 0);
    }

    boolean remove(long key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(long, int)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Long)) return false;
        if (value != null && !(value instanceof Integer)) return false;
        return this.remove(key != null ? ((Long) key).longValue() : (long) 0, value != null ? ((Integer) value).intValue() : (int) 0);
    }

    int replace(long key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(long, int)
     */
    @Deprecated
    default Integer replace(Long key, Integer value) {
        return this.replace(key != null ? key.longValue() : (long) 0, value != null ? value.intValue() : (int) 0);
    }

    boolean replace(long key, int oldValue, int newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(long, int, int)
     */
    @Deprecated
    default boolean replace(Long key, Integer oldValue, Integer newValue) {
        return this.replace(key != null ? key.longValue() : (long) 0, oldValue != null ? oldValue.intValue() : (int) 0, newValue != null ? newValue.intValue() : (int) 0);
    }

    int computeIfAbsent(long key, @NotNull LongFunction<? extends Integer> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(long, LongFunction)
     */
    @Deprecated
    default Integer computeIfAbsent(Long key, @NotNull Function<? super Long, ? extends Integer> mappingFunction) {
        return this.computeIfAbsent(key != null ? key.longValue() : (long) 0, (Long l_t) -> mappingFunction.apply(l_t));
    }

    int computeIfPresent(long key, @NotNull LongIntBiFunction<? extends Integer> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(long, LongIntBiFunction)
     */
    @Deprecated
    default Integer computeIfPresent(Long key, @NotNull BiFunction<? super Long, ? super Integer, ? extends Integer> remappingFunction) {
        return this.computeIfPresent(key != null ? key.longValue() : (long) 0, (long l_t, int l_u) -> remappingFunction.apply(l_t, l_u));
    }

    int compute(long key, @NotNull LongIntBiFunction<? extends Integer> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(long, LongIntBiFunction)
     */
    @Deprecated
    default Integer compute(Long key, @NotNull BiFunction<? super Long, ? super Integer, ? extends Integer> remappingFunction) {
        return this.compute(key != null ? key.longValue() : (long) 0, (long l_t, int l_u) -> remappingFunction.apply(l_t, l_u));
    }

    int merge(long key, int value, @NotNull IntIntBiFunction<? extends Integer> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(long, int, IntIntBiFunction)
     */
    @Deprecated
    default Integer merge(Long key, @NotNull Integer value, @NotNull BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        return this.merge(key != null ? key.longValue() : (long) 0, value.intValue(), (int l_t, int l_u) -> remappingFunction.apply(l_t, l_u));
    }

    int update(long key, @NotNull IntUnaryOperator updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(long, IntUnaryOperator)
     */
    @Deprecated
    default Integer update(Long key, @NotNull Function<? super Integer, ? extends Integer> updateFunction) {
        return this.update(key != null ? key.longValue() : (long) 0, (int l_t) -> {
            final Integer result;
            return (result = updateFunction.apply(l_t)) != null ? result : (int) 0;
        });
    }

    int remapping(long key, @NotNull LongIntBiFunction<? extends Long> remappingFunction, IntIntBiFunction<? extends Integer> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(long, LongIntBiFunction, IntIntBiFunction)
     */
    @Deprecated
    default Integer remapping(Long key, @NotNull BiFunction<? super Long, ? super Integer, ? extends Long> remappingFunction, @NotNull BiFunction<? super Integer, ? super Integer, ? extends Integer> mergeFunction) {
        return this.remapping(key != null ? key.longValue() : (long) 0, (long l_t, int l_u) -> remappingFunction.apply(l_t, l_u), (int l_t, int l_u) -> mergeFunction.apply(l_t, l_u));
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

    void forEach(@NotNull LongIntBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(LongIntBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Long, ? super Integer> action) {
        this.forEach((long l_t, int l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull LongIntBiOperator function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(LongIntBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Long, ? super Integer, ? extends Integer> function) {
        this.replaceAll((long l_t, int l_u) -> function.apply(l_t, l_u));
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
    LongSet keySet();

    @NotNull
    IntCollection values();

    @NotNull
    EntrySetExt<LongIntMap.Entry> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<MapExt.Entry<Long, Integer>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface Entry extends MapExt.Entry<Long, Integer> {
        long getKeyRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getKeyRaw()
         */
        @Deprecated
        default Long getKey() {
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
