package io.github.shiozakanaptr.acext.api.collection.shorttype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.objecttype.ObjCollection;
import io.github.shiozakanaptr.acext.util.function.shorttype.*;
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
public interface ShortObjMap<V> extends MapExt<Short, V> {
    V get(short key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #get(short)
     */
    @Deprecated
    default V get(Object key) {
        if (key != null && !(key instanceof Short)) return this.getDefaultReturnValue();
        return this.get(key != null ? ((Short) key).shortValue() : (short) 0);
    }

    V getOrDefault(short key, V defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(short, V)
     */
    @Deprecated
    default V getOrDefault(Object key, V defaultValue) {
        if (key != null && !(key instanceof Short)) return defaultValue;
        return this.getOrDefault(key != null ? ((Short) key).shortValue() : (short) 0, defaultValue);
    }

    V put(short key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(short, V)
     */
    @Deprecated
    default V put(Short key, V value) {
        return this.put(key != null ? key.shortValue() : (short) 0, value);
    }

    V putIfAbsent(short key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(short, V)
     */
    @Deprecated
    default V putIfAbsent(Short key, V value) {
        return this.putIfAbsent(key != null ? key.shortValue() : (short) 0, value);
    }

    V remove(short key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(short)
     */
    @Deprecated
    default V remove(Object key) {
        if (key != null && !(key instanceof Short)) return this.getDefaultReturnValue();
        return this.remove(key != null ? ((Short) key).shortValue() : (short) 0);
    }

    boolean remove(short key, Object value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(short, Object)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Short)) return false;
        return this.remove(key != null ? ((Short) key).shortValue() : (short) 0, value);
    }

    V replace(short key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(short, V)
     */
    @Deprecated
    default V replace(Short key, V value) {
        return this.replace(key != null ? key.shortValue() : (short) 0, value);
    }

    boolean replace(short key, V oldValue, V newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(short, V, V)
     */
    @Deprecated
    default boolean replace(Short key, V oldValue, V newValue) {
        return this.replace(key != null ? key.shortValue() : (short) 0, oldValue, newValue);
    }

    V computeIfAbsent(short key, @NotNull ShortFunction<? extends V> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(short, ShortFunction)
     */
    @Deprecated
    default V computeIfAbsent(Short key, @NotNull Function<? super Short, ? extends V> mappingFunction) {
        return this.computeIfAbsent(key != null ? key.shortValue() : (short) 0, (Short l_t) -> mappingFunction.apply(l_t));
    }

    V computeIfPresent(short key, @NotNull ShortObjBiFunction<? super V, ? extends V> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(short, ShortObjBiFunction)
     */
    @Deprecated
    default V computeIfPresent(Short key, @NotNull BiFunction<? super Short, ? super V, ? extends V> remappingFunction) {
        return this.computeIfPresent(key != null ? key.shortValue() : (short) 0, (short l_t, V l_u) -> remappingFunction.apply(l_t, l_u));
    }

    V compute(short key, @NotNull ShortObjBiFunction<? super V, ? extends V> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(short, ShortObjBiFunction)
     */
    @Deprecated
    default V compute(Short key, @NotNull BiFunction<? super Short, ? super V, ? extends V> remappingFunction) {
        return this.compute(key != null ? key.shortValue() : (short) 0, (short l_t, V l_u) -> remappingFunction.apply(l_t, l_u));
    }

    V merge(short key, V value, @NotNull BiFunction<? super V, ? super V, ? extends V> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(short, V, BiFunction)
     */
    @Deprecated
    default V merge(Short key, @NotNull V value, @NotNull BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return this.merge(key != null ? key.shortValue() : (short) 0, value, (V l_t, V l_u) -> remappingFunction.apply(l_t, l_u));
    }

    V update(short key, @NotNull Function<? super V, ? extends V> updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(short, Function)
     */
    @Deprecated
    default V update(Short key, @NotNull Function<? super V, ? extends V> updateFunction) {
        return this.update(key != null ? key.shortValue() : (short) 0, (V l_t) -> updateFunction.apply(l_t));
    }

    V remapping(short key, @NotNull ShortObjBiFunction<? super V, ? extends Short> remappingFunction, BiFunction<? super V, ? super V, ? extends V> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(short, ShortObjBiFunction, BiFunction)
     */
    @Deprecated
    default V remapping(Short key, @NotNull BiFunction<? super Short, ? super V, ? extends Short> remappingFunction, @NotNull BiFunction<? super V, ? super V, ? extends V> mergeFunction) {
        return this.remapping(key != null ? key.shortValue() : (short) 0, (short l_t, V l_u) -> remappingFunction.apply(l_t, l_u), (V l_t, V l_u) -> mergeFunction.apply(l_t, l_u));
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

    void forEach(@NotNull ShortObjBiConsumer<? super V> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(ShortObjBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Short, ? super V> action) {
        this.forEach((short l_t, V l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull ShortObjBiFunction<? super V, ? extends V> function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(ShortObjBiFunction)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Short, ? super V, ? extends V> function) {
        this.replaceAll((short l_t, V l_u) -> function.apply(l_t, l_u));
    }

    @NotNull
    ShortSet keySet();

    @NotNull
    ObjCollection<V> values();

    @NotNull
    EntrySetExt<ShortObjMap.Entry<V>> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<MapExt.Entry<Short, V>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface Entry<V> extends MapExt.Entry<Short, V> {
        short getKeyRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getKeyRaw()
         */
        @Deprecated
        default Short getKey() {
            return this.getKeyRaw();
        }

    }

}
