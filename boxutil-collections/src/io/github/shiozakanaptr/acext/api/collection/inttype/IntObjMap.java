package io.github.shiozakanaptr.acext.api.collection.inttype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.objecttype.ObjCollection;
import io.github.shiozakanaptr.acext.util.function.inttype.*;
import io.github.shiozakanaptr.acext.util.function.objecttype.*;
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
public interface IntObjMap<V> extends MapExt<Integer, V> {
    V get(int key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #get(int)
     */
    @Deprecated
    default V get(Object key) {
        if (key != null && !(key instanceof Integer)) return this.getDefaultReturnValue();
        return this.get(key != null ? ((Integer) key).intValue() : (int) 0);
    }

    V getOrDefault(int key, V defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(int, V)
     */
    @Deprecated
    default V getOrDefault(Object key, V defaultValue) {
        if (key != null && !(key instanceof Integer)) return defaultValue;
        return this.getOrDefault(key != null ? ((Integer) key).intValue() : (int) 0, defaultValue);
    }

    V put(int key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(int, V)
     */
    @Deprecated
    default V put(Integer key, V value) {
        return this.put(key != null ? key.intValue() : (int) 0, value);
    }

    V putIfAbsent(int key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(int, V)
     */
    @Deprecated
    default V putIfAbsent(Integer key, V value) {
        return this.putIfAbsent(key != null ? key.intValue() : (int) 0, value);
    }

    V remove(int key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(int)
     */
    @Deprecated
    default V remove(Object key) {
        if (key != null && !(key instanceof Integer)) return this.getDefaultReturnValue();
        return this.remove(key != null ? ((Integer) key).intValue() : (int) 0);
    }

    boolean remove(int key, Object value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(int, Object)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Integer)) return false;
        return this.remove(key != null ? ((Integer) key).intValue() : (int) 0, value);
    }

    V replace(int key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(int, V)
     */
    @Deprecated
    default V replace(Integer key, V value) {
        return this.replace(key != null ? key.intValue() : (int) 0, value);
    }

    boolean replace(int key, V oldValue, V newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(int, V, V)
     */
    @Deprecated
    default boolean replace(Integer key, V oldValue, V newValue) {
        return this.replace(key != null ? key.intValue() : (int) 0, oldValue, newValue);
    }

    V computeIfAbsent(int key, @NotNull IntFunction<? extends V> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(int, IntFunction)
     */
    @Deprecated
    default V computeIfAbsent(Integer key, @NotNull Function<? super Integer, ? extends V> mappingFunction) {
        return this.computeIfAbsent(key != null ? key.intValue() : (int) 0, (Integer l_t) -> mappingFunction.apply(l_t));
    }

    V computeIfPresent(int key, @NotNull IntObjBiFunction<? super V, ? extends V> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(int, IntObjBiFunction)
     */
    @Deprecated
    default V computeIfPresent(Integer key, @NotNull BiFunction<? super Integer, ? super V, ? extends V> remappingFunction) {
        return this.computeIfPresent(key != null ? key.intValue() : (int) 0, (int l_t, V l_u) -> remappingFunction.apply(l_t, l_u));
    }

    V compute(int key, @NotNull IntObjBiFunction<? super V, ? extends V> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(int, IntObjBiFunction)
     */
    @Deprecated
    default V compute(Integer key, @NotNull BiFunction<? super Integer, ? super V, ? extends V> remappingFunction) {
        return this.compute(key != null ? key.intValue() : (int) 0, (int l_t, V l_u) -> remappingFunction.apply(l_t, l_u));
    }

    V merge(int key, V value, @NotNull BiFunction<? super V, ? super V, ? extends V> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(int, V, BiFunction)
     */
    @Deprecated
    default V merge(Integer key, @NotNull V value, @NotNull BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return this.merge(key != null ? key.intValue() : (int) 0, value, (V l_t, V l_u) -> remappingFunction.apply(l_t, l_u));
    }

    V update(int key, @NotNull Function<? super V, ? extends V> updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(int, Function)
     */
    @Deprecated
    default V update(Integer key, @NotNull Function<? super V, ? extends V> updateFunction) {
        return this.update(key != null ? key.intValue() : (int) 0, (V l_t) -> updateFunction.apply(l_t));
    }

    V remapping(int key, @NotNull IntObjBiFunction<? super V, ? extends Integer> remappingFunction, BiFunction<? super V, ? super V, ? extends V> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(int, IntObjBiFunction, BiFunction)
     */
    @Deprecated
    default V remapping(Integer key, @NotNull BiFunction<? super Integer, ? super V, ? extends Integer> remappingFunction, @NotNull BiFunction<? super V, ? super V, ? extends V> mergeFunction) {
        return this.remapping(key != null ? key.intValue() : (int) 0, (int l_t, V l_u) -> remappingFunction.apply(l_t, l_u), (V l_t, V l_u) -> mergeFunction.apply(l_t, l_u));
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

    void forEach(@NotNull IntObjBiConsumer<? super V> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(IntObjBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Integer, ? super V> action) {
        this.forEach((int l_t, V l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull IntObjBiFunction<? super V, ? extends V> function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(IntObjBiFunction)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Integer, ? super V, ? extends V> function) {
        this.replaceAll((int l_t, V l_u) -> function.apply(l_t, l_u));
    }

    @NotNull
    IntSet keySet();

    @NotNull
    ObjCollection<V> values();

    @NotNull
    EntrySetExt<IntObjMap.Entry<V>> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<MapExt.Entry<Integer, V>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface Entry<V> extends MapExt.Entry<Integer, V> {
        int getKeyRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getKeyRaw()
         */
        @Deprecated
        default Integer getKey() {
            return this.getKeyRaw();
        }
    }

}
