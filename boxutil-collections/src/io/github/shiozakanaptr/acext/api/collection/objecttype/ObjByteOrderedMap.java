package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.bytetype.ByteOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.objecttype.ObjByteBiConsumer;
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
public interface ObjByteOrderedMap<K> extends ObjByteMap<K>, OrderedMap<K, Byte> {
    ObjByteMap.Entry<K> firstEntry();

    boolean forFirst(@NotNull ObjByteBiConsumer<? super K> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(ObjByteBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super K, ? super Byte> action) {
        return this.forFirst((K l_t, byte l_v) -> action.accept(l_t, l_v));
    }

    ObjByteMap.Entry<K> lastEntry();

    boolean forLast(@NotNull ObjByteBiConsumer<? super K> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(ObjByteBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super K, ? super Byte> action) {
        return this.forLast((K l_t, byte l_v) -> action.accept(l_t, l_v));
    }


    byte putFirst(K key, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(K, byte)
     */
    @Deprecated
    default Byte putFirst(K key, Byte value) {
        return this.putFirst(key, value != null ? value.byteValue() : (byte) 0);
    }

    byte putLast(K key, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(K, byte)
     */
    @Deprecated
    default Byte putLast(K key, Byte value) {
        return this.putLast(key, value != null ? value.byteValue() : (byte) 0);
    }

    ObjByteMap.Entry<K> pollFirstEntry();

    boolean pollFirst(@NotNull ObjByteBiConsumer<? super K> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(ObjByteBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super K, ? super Byte> action) {
        return this.pollFirst((K l_t, byte l_v) -> action.accept(l_t, l_v));
    }

    ObjByteMap.Entry<K> pollLastEntry();

    boolean pollLast(@NotNull ObjByteBiConsumer<? super K> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(ObjByteBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super K, ? super Byte> action) {
        return this.pollLast((K l_t, byte l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull ObjByteBiConsumer<? super K> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(ObjByteBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super K, ? super Byte> action) {
        this.forEachReversed((K l_t, byte l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    ObjOrderedSet<K> keySet();

    @NotNull
    ByteOrderedCollection values();

    @NotNull
    OrderedEntrySet<ObjByteMap.Entry<K>> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<K, Byte>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
