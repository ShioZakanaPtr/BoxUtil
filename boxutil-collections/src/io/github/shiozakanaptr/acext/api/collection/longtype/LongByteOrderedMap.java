package io.github.shiozakanaptr.acext.api.collection.longtype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.bytetype.ByteOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.longtype.LongByteBiConsumer;
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
public interface LongByteOrderedMap extends LongByteMap, OrderedMap<Long, Byte> {
    LongByteMap.Entry firstEntry();

    boolean forFirst(@NotNull LongByteBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(LongByteBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Long, ? super Byte> action) {
        return this.forFirst((long l_t, byte l_v) -> action.accept(l_t, l_v));
    }

    LongByteMap.Entry lastEntry();

    boolean forLast(@NotNull LongByteBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(LongByteBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Long, ? super Byte> action) {
        return this.forLast((long l_t, byte l_v) -> action.accept(l_t, l_v));
    }


    byte putFirst(long key, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(long, byte)
     */
    @Deprecated
    default Byte putFirst(Long key, Byte value) {
        return this.putFirst(key != null ? key.longValue() : (long) 0, value != null ? value.byteValue() : (byte) 0);
    }

    byte putLast(long key, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(long, byte)
     */
    @Deprecated
    default Byte putLast(Long key, Byte value) {
        return this.putLast(key != null ? key.longValue() : (long) 0, value != null ? value.byteValue() : (byte) 0);
    }

    LongByteMap.Entry pollFirstEntry();

    boolean pollFirst(@NotNull LongByteBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(LongByteBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Long, ? super Byte> action) {
        return this.pollFirst((long l_t, byte l_v) -> action.accept(l_t, l_v));
    }

    LongByteMap.Entry pollLastEntry();

    boolean pollLast(@NotNull LongByteBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(LongByteBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Long, ? super Byte> action) {
        return this.pollLast((long l_t, byte l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull LongByteBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(LongByteBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Long, ? super Byte> action) {
        this.forEachReversed((long l_t, byte l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    LongOrderedSet keySet();

    @NotNull
    ByteOrderedCollection values();

    @NotNull
    OrderedEntrySet<LongByteMap.Entry> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Long, Byte>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
