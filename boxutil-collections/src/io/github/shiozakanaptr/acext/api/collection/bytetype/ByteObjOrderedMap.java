package io.github.shiozakanaptr.acext.api.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.objecttype.ObjOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.bytetype.ByteObjBiConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

/**
 * Type specialization interface.
 *
 * @see OrderedMap
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface ByteObjOrderedMap<V> extends ByteObjMap<V>, OrderedMap<Byte, V> {
    ByteObjMap.Entry<V> firstEntry();

    boolean forFirst(@NotNull ByteObjBiConsumer<? super V> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(ByteObjBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Byte, ? super V> action) {
        return this.forFirst((byte l_t, V l_v) -> action.accept(l_t, l_v));
    }

    ByteObjMap.Entry<V> lastEntry();

    boolean forLast(@NotNull ByteObjBiConsumer<? super V> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(ByteObjBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Byte, ? super V> action) {
        return this.forLast((byte l_t, V l_v) -> action.accept(l_t, l_v));
    }


    V putFirst(byte key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(byte, V)
     */
    @Deprecated
    default V putFirst(Byte key, V value) {
        return this.putFirst(key != null ? key.byteValue() : (byte) 0, value);
    }

    V putLast(byte key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(byte, V)
     */
    @Deprecated
    default V putLast(Byte key, V value) {
        return this.putLast(key != null ? key.byteValue() : (byte) 0, value);
    }

    ByteObjMap.Entry<V> pollFirstEntry();

    boolean pollFirst(@NotNull ByteObjBiConsumer<? super V> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(ByteObjBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Byte, ? super V> action) {
        return this.pollFirst((byte l_t, V l_v) -> action.accept(l_t, l_v));
    }

    ByteObjMap.Entry<V> pollLastEntry();

    boolean pollLast(@NotNull ByteObjBiConsumer<? super V> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(ByteObjBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Byte, ? super V> action) {
        return this.pollLast((byte l_t, V l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull ByteObjBiConsumer<? super V> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(ByteObjBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Byte, ? super V> action) {
        this.forEachReversed((byte l_t, V l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    ByteOrderedSet keySet();

    @NotNull
    ObjOrderedCollection<V> values();

    @NotNull
    OrderedEntrySet<ByteObjMap.Entry<V>> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Byte, V>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
