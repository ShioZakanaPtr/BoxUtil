package io.github.shiozakanaptr.acext.api.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.objecttype.ObjCollection;
import io.github.shiozakanaptr.acext.util.function.bytetype.*;
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
public interface ByteObjMap<V> extends MapExt<Byte, V> {
    V get(byte key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #get(byte)
     */
    @Deprecated
    default V get(Object key) {
        if (key != null && !(key instanceof Byte)) return this.getDefaultReturnValue();
        return this.get(key != null ? ((Byte) key).byteValue() : (byte) 0);
    }

    V getOrDefault(byte key, V defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(byte, V)
     */
    @Deprecated
    default V getOrDefault(Object key, V defaultValue) {
        if (key != null && !(key instanceof Byte)) return defaultValue;
        return this.getOrDefault(key != null ? ((Byte) key).byteValue() : (byte) 0, defaultValue);
    }

    V put(byte key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(byte, V)
     */
    @Deprecated
    default V put(Byte key, V value) {
        return this.put(key != null ? key.byteValue() : (byte) 0, value);
    }

    V putIfAbsent(byte key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(byte, V)
     */
    @Deprecated
    default V putIfAbsent(Byte key, V value) {
        return this.putIfAbsent(key != null ? key.byteValue() : (byte) 0, value);
    }

    V remove(byte key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(byte)
     */
    @Deprecated
    default V remove(Object key) {
        if (key != null && !(key instanceof Byte)) return this.getDefaultReturnValue();
        return this.remove(key != null ? ((Byte) key).byteValue() : (byte) 0);
    }

    boolean remove(byte key, Object value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(byte, Object)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Byte)) return false;
        return this.remove(key != null ? ((Byte) key).byteValue() : (byte) 0, value);
    }

    V replace(byte key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(byte, V)
     */
    @Deprecated
    default V replace(Byte key, V value) {
        return this.replace(key != null ? key.byteValue() : (byte) 0, value);
    }

    boolean replace(byte key, V oldValue, V newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(byte, V, V)
     */
    @Deprecated
    default boolean replace(Byte key, V oldValue, V newValue) {
        return this.replace(key != null ? key.byteValue() : (byte) 0, oldValue, newValue);
    }

    V computeIfAbsent(byte key, @NotNull ByteFunction<? extends V> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(byte, ByteFunction)
     */
    @Deprecated
    default V computeIfAbsent(Byte key, @NotNull Function<? super Byte, ? extends V> mappingFunction) {
        return this.computeIfAbsent(key != null ? key.byteValue() : (byte) 0, (Byte l_t) -> mappingFunction.apply(l_t));
    }

    V computeIfPresent(byte key, @NotNull ByteObjBiFunction<? super V, ? extends V> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(byte, ByteObjBiFunction)
     */
    @Deprecated
    default V computeIfPresent(Byte key, @NotNull BiFunction<? super Byte, ? super V, ? extends V> remappingFunction) {
        return this.computeIfPresent(key != null ? key.byteValue() : (byte) 0, (byte l_t, V l_u) -> remappingFunction.apply(l_t, l_u));
    }

    V compute(byte key, @NotNull ByteObjBiFunction<? super V, ? extends V> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(byte, ByteObjBiFunction)
     */
    @Deprecated
    default V compute(Byte key, @NotNull BiFunction<? super Byte, ? super V, ? extends V> remappingFunction) {
        return this.compute(key != null ? key.byteValue() : (byte) 0, (byte l_t, V l_u) -> remappingFunction.apply(l_t, l_u));
    }

    V merge(byte key, V value, @NotNull BiFunction<? super V, ? super V, ? extends V> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(byte, V, BiFunction)
     */
    @Deprecated
    default V merge(Byte key, @NotNull V value, @NotNull BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return this.merge(key != null ? key.byteValue() : (byte) 0, value, (V l_t, V l_u) -> remappingFunction.apply(l_t, l_u));
    }

    V update(byte key, @NotNull Function<? super V, ? extends V> updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(byte, Function)
     */
    @Deprecated
    default V update(Byte key, @NotNull Function<? super V, ? extends V> updateFunction) {
        return this.update(key != null ? key.byteValue() : (byte) 0, (V l_t) -> updateFunction.apply(l_t));
    }

    V remapping(byte key, @NotNull ByteObjBiFunction<? super V, ? extends Byte> remappingFunction, BiFunction<? super V, ? super V, ? extends V> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(byte, ByteObjBiFunction, BiFunction)
     */
    @Deprecated
    default V remapping(Byte key, @NotNull BiFunction<? super Byte, ? super V, ? extends Byte> remappingFunction, @NotNull BiFunction<? super V, ? super V, ? extends V> mergeFunction) {
        return this.remapping(key != null ? key.byteValue() : (byte) 0, (byte l_t, V l_u) -> remappingFunction.apply(l_t, l_u), (V l_t, V l_u) -> mergeFunction.apply(l_t, l_u));
    }

    boolean swapAt(byte from, byte to);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #swapAt(byte, byte)
     */
    @Deprecated
    default boolean swapAt(Byte from, Byte to) {
        return this.swapAt(from != null ? from.byteValue() : (byte) 0, to != null ? to.byteValue() : (byte) 0);
    }

    boolean containsKey(byte key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #containsKey(byte)
     */
    @Deprecated
    default boolean containsKey(Object key) {
        if (key != null && !(key instanceof Byte)) return false;
        return this.containsKey(key != null ? ((Byte) key).byteValue() : (byte) 0);
    }

    void forEach(@NotNull ByteObjBiConsumer<? super V> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(ByteObjBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Byte, ? super V> action) {
        this.forEach((byte l_t, V l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull ByteObjBiFunction<? super V, ? extends V> function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(ByteObjBiFunction)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Byte, ? super V, ? extends V> function) {
        this.replaceAll((byte l_t, V l_u) -> function.apply(l_t, l_u));
    }

    @NotNull
    ByteSet keySet();

    @NotNull
    ObjCollection<V> values();

    @NotNull
    EntrySetExt<ByteObjMap.Entry<V>> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<MapExt.Entry<Byte, V>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface Entry<V> extends MapExt.Entry<Byte, V> {
        byte getKeyRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getKeyRaw()
         */
        @Deprecated
        default Byte getKey() {
            return this.getKeyRaw();
        }

    }

}
