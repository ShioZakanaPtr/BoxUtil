package io.github.shiozakanaptr.acext.api.collection.longtype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.longtype.LongSet;
import io.github.shiozakanaptr.acext.api.collection.objecttype.ObjCollection;
import io.github.shiozakanaptr.acext.util.function.longtype.*;
import io.github.shiozakanaptr.acext.util.function.objecttype.*;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.function.BiConsumer;

public interface LongObjMap<V> extends MapExt<Long, V> {
    V get(long key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #get(long)
     */
    @Deprecated
    default V get(Object key) {
        if (key != null && !(key instanceof Long)) return this.getDefaultReturnValue();
        return this.get(key != null ? ((Long) key).longValue() : (long) 0);
    }

    V getOrDefault(long key, V defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(long, V)
     */
    @Deprecated
    default V getOrDefault(Object key, V defaultValue) {
        if (key != null && !(key instanceof Long)) return defaultValue;
        return this.getOrDefault(key != null ? ((Long) key).longValue() : (long) 0, defaultValue);
    }

    V put(long key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(long, V)
     */
    @Deprecated
    default V put(Long key, V value) {
        return this.put(key != null ? key.longValue() : (long) 0, value);
    }

    V putIfAbsent(long key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(long, V)
     */
    @Deprecated
    default V putIfAbsent(Long key, V value) {
        return this.putIfAbsent(key != null ? key.longValue() : (long) 0, value);
    }

    V remove(long key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(long)
     */
    @Deprecated
    default V remove(Object key) {
        if (key != null && !(key instanceof Long)) return this.getDefaultReturnValue();
        return this.remove(key != null ? ((Long) key).longValue() : (long) 0);
    }

    boolean remove(long key, Object value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(long, Object)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Long)) return false;
        return this.remove(key != null ? ((Long) key).longValue() : (long) 0, value);
    }

    V replace(long key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(long, V)
     */
    @Deprecated
    default V replace(Long key, V value) {
        return this.replace(key != null ? key.longValue() : (long) 0, value);
    }

    boolean replace(long key, V oldValue, V newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(long, V, V)
     */
    @Deprecated
    default boolean replace(Long key, V oldValue, V newValue) {
        return this.replace(key != null ? key.longValue() : (long) 0, oldValue, newValue);
    }

    V computeIfAbsent(long key, @NotNull LongFunction<? extends V> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(long, LongFunction)
     */
    @Deprecated
    default V computeIfAbsent(Long key, @NotNull Function<? super Long, ? extends V> mappingFunction) {
        return this.computeIfAbsent(key != null ? key.longValue() : (long) 0, (Long l_t) -> mappingFunction.apply(l_t));
    }

    V computeIfPresent(long key, @NotNull LongObjBiFunction<? super V, ? extends V> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(long, LongObjBiFunction)
     */
    @Deprecated
    default V computeIfPresent(Long key, @NotNull BiFunction<? super Long, ? super V, ? extends V> remappingFunction) {
        return this.computeIfPresent(key != null ? key.longValue() : (long) 0, (long l_t, V l_u) -> remappingFunction.apply(l_t, l_u));
    }

    V compute(long key, @NotNull LongObjBiFunction<? super V, ? extends V> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(long, LongObjBiFunction)
     */
    @Deprecated
    default V compute(Long key, @NotNull BiFunction<? super Long, ? super V, ? extends V> remappingFunction) {
        return this.compute(key != null ? key.longValue() : (long) 0, (long l_t, V l_u) -> remappingFunction.apply(l_t, l_u));
    }

    V merge(long key, V value, @NotNull BiFunction<? super V, ? super V, ? extends V> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(long, V, BiFunction)
     */
    @Deprecated
    default V merge(Long key, @NotNull V value, @NotNull BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return this.merge(key != null ? key.longValue() : (long) 0, value, (V l_t, V l_u) -> remappingFunction.apply(l_t, l_u));
    }

    V update(long key, @NotNull Function<? super V, ? extends V> updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(long, Function)
     */
    @Deprecated
    default V update(Long key, @NotNull Function<? super V, ? extends V> updateFunction) {
        return this.update(key != null ? key.longValue() : (long) 0, (V l_t) -> updateFunction.apply(l_t));
    }

    V remapping(long key, @NotNull LongObjBiFunction<? super V, ? extends Long> remappingFunction, BiFunction<? super V, ? super V, ? extends V> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(long, LongObjBiFunction, BiFunction)
     */
    @Deprecated
    default V remapping(Long key, @NotNull BiFunction<? super Long, ? super V, ? extends Long> remappingFunction, @NotNull BiFunction<? super V, ? super V, ? extends V> mergeFunction) {
        return this.remapping(key != null ? key.longValue() : (long) 0, (long l_t, V l_u) -> remappingFunction.apply(l_t, l_u), (V l_t, V l_u) -> mergeFunction.apply(l_t, l_u));
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

    void forEach(@NotNull LongObjBiConsumer<? super V> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(LongObjBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Long, ? super V> action) {
        this.forEach((long l_t, V l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull LongObjBiFunction<? super V, ? extends V> function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(LongObjBiFunction)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Long, ? super V, ? extends V> function) {
        this.replaceAll((long l_t, V l_u) -> function.apply(l_t, l_u));
    }

    @NotNull
    LongSet keySet();

    @NotNull
    ObjCollection<V> values();

    @NotNull
    EntrySetExt<LongObjMap.Entry<V>> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<MapExt.Entry<Long, V>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface Entry<V> extends MapExt.Entry<Long, V> {
        long getKeyRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getKeyRaw()
         */
        @Deprecated
        default Long getKey() {
            return this.getKeyRaw();
        }

    }

}
