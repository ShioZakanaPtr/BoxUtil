package io.github.shiozakanaptr.acext.api.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.longtype.LongOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.bytetype.ByteLongBiConsumer;
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
public interface ByteLongOrderedMap extends ByteLongMap, OrderedMap<Byte, Long> {
    ByteLongMap.Entry firstEntry();

    boolean forFirst(@NotNull ByteLongBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(ByteLongBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Byte, ? super Long> action) {
        return this.forFirst((byte l_t, long l_v) -> action.accept(l_t, l_v));
    }

    ByteLongMap.Entry lastEntry();

    boolean forLast(@NotNull ByteLongBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(ByteLongBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Byte, ? super Long> action) {
        return this.forLast((byte l_t, long l_v) -> action.accept(l_t, l_v));
    }


    long putFirst(byte key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(byte, long)
     */
    @Deprecated
    default Long putFirst(Byte key, Long value) {
        return this.putFirst(key != null ? key.byteValue() : (byte) 0, value != null ? value.longValue() : (long) 0);
    }

    long putLast(byte key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(byte, long)
     */
    @Deprecated
    default Long putLast(Byte key, Long value) {
        return this.putLast(key != null ? key.byteValue() : (byte) 0, value != null ? value.longValue() : (long) 0);
    }

    ByteLongMap.Entry pollFirstEntry();

    boolean pollFirst(@NotNull ByteLongBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(ByteLongBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Byte, ? super Long> action) {
        return this.pollFirst((byte l_t, long l_v) -> action.accept(l_t, l_v));
    }

    ByteLongMap.Entry pollLastEntry();

    boolean pollLast(@NotNull ByteLongBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(ByteLongBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Byte, ? super Long> action) {
        return this.pollLast((byte l_t, long l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull ByteLongBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(ByteLongBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Byte, ? super Long> action) {
        this.forEachReversed((byte l_t, long l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    ByteOrderedSet keySet();

    @NotNull
    LongOrderedCollection values();

    @NotNull
    OrderedEntrySet<ByteLongMap.Entry> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Byte, Long>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
