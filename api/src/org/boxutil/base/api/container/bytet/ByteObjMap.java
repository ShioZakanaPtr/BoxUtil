package org.boxutil.base.api.container.bytet;

import org.boxutil.base.api.container.MapExt;
import org.boxutil.base.api.container.SetExt;
import org.boxutil.util.function.bytet.ByteFunction;
import org.boxutil.util.function.bytet.ByteObjBiConsumer;
import org.boxutil.util.function.bytet.ByteObjBiFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.*;

public interface ByteObjMap<V> extends MapExt<Byte, V> {
    V get(byte key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default V get(Object key) {
        if (key != null && !(key instanceof Byte)) return this.getDefaultReturnValue();
        return this.get(key != null ? ((Byte) key).byteValue() : (byte) 0);
    }

    V getOrDefault(byte key, V defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default V getOrDefault(Object key, V defaultValue) {
        if (key != null && !(key instanceof Byte)) return defaultValue;
        return this.getOrDefault(key != null ? ((Byte) key).byteValue() : (byte) 0, defaultValue);
    }

    V put(byte key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default @Nullable V put(Byte key, V value) {
        return this.put(key != null ? key : (byte) 0, value);
    }

    V putIfAbsent(byte key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default V putIfAbsent(Byte key, V value) {
        return this.putIfAbsent(key != null ? key : (byte) 0, value);
    }

    V remove(byte key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default V remove(Object key) {
        if (key != null && !(key instanceof Byte)) return this.getDefaultReturnValue();
        return this.remove(key != null ? ((Byte) key).byteValue() : (byte) 0);
    }

    boolean remove(byte key, Object value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Byte)) return false;
        return this.remove(key != null ? ((Byte) key).byteValue() : (byte) 0, value);
    }

    V replace(byte key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default V replace(Byte key, V value) {
        return this.replace(key == null ? (byte) 0 : key, value);
    }

    boolean replace(byte key, V oldValue, V newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean replace(Byte key, V oldValue, V newValue) {
        return this.replace(key != null ? key : (byte) 0, oldValue, newValue);
    }

    V computeIfAbsent(byte key, @NotNull ByteFunction<? extends V> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default V computeIfAbsent(Byte key, @NotNull Function<? super Byte, ? extends V> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        return this.computeIfAbsent(key != null ? key : (byte) 0, (byte l_t) -> mappingFunction.apply(l_t));
    }

    V computeIfPresent(byte key, @NotNull ByteObjBiFunction<? super V, ? extends V> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default V computeIfPresent(Byte key, @NotNull BiFunction<? super Byte, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        return this.computeIfPresent(key != null ? key : (byte) 0, (byte l_t, V l_u) -> remappingFunction.apply(l_t, l_u));
    }

    V compute(byte key, @NotNull ByteObjBiFunction<? super V, ? extends V> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default V compute(Byte key, @NotNull BiFunction<? super Byte, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        return this.compute(key != null ? key : (byte) 0, (byte l_t, V l_u) -> remappingFunction.apply(l_t, l_u));
    }

    V merge(byte key, @NotNull V value, @NotNull BiFunction<? super V, ? super V, ? extends V> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default V merge(Byte key, @NotNull V value, @NotNull BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(value);
        Objects.requireNonNull(remappingFunction);
        return this.merge(key != null ? key : (byte) 0, value, (V l_t, V l_u) -> remappingFunction.apply(l_t, l_u));
    }

    V remapping(byte key, ByteObjBiFunction<? super V, ? extends Byte> remappingFunction, BiFunction<? super V, ? super V, ? extends V> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default V remapping(Byte key, @NotNull BiFunction<? super Byte, ? super V, ? extends Byte> remappingFunction, @NotNull BiFunction<? super V, ? super V, ? extends V> mergeFunction) {
        Objects.requireNonNull(remappingFunction);
        Objects.requireNonNull(mergeFunction);
        return this.remapping(key != null ? key : (byte) 0, (byte l_t, V l_u) -> remappingFunction.apply(l_t, l_u), (V l_t, V l_u) -> mergeFunction.apply(l_t, l_u));
    }

    boolean containsKey(byte key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean containsKey(Object key) {
        if (key != null && !(key instanceof Byte)) return false;
        return this.containsKey(key != null ? ((Byte) key).byteValue() : (byte) 0);
    }

    void forEach(ByteObjBiConsumer<? super V> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void forEach(BiConsumer<? super Byte, ? super V> action) {
        Objects.requireNonNull(action);
        this.forEach((byte l_t, V l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(ByteObjBiFunction<? super V, ? extends V> function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void replaceAll(BiFunction<? super Byte, ? super V, ? extends V> function) {
        Objects.requireNonNull(function);
        this.replaceAll((byte l_t, V l_u) -> function.apply(l_t, l_u));
    }

    @NotNull ByteSet keySet();

    @NotNull SetExt<ByteObjMap.Entry<V>> rawEntrySet();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default @NotNull SetExt<Map.Entry<Byte, V>> entrySet() {
        return (SetExt) this.rawEntrySet();
    }

    interface Entry<V> extends Map.Entry<Byte, V> {
        byte getRawKey();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         */
        @Deprecated
        default Byte getKey() {
            return this.getRawKey();
        }
    }
}
