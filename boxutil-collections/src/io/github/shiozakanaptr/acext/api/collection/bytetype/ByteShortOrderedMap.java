package io.github.shiozakanaptr.acext.api.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.shorttype.ShortOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.bytetype.ByteShortBiConsumer;
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
public interface ByteShortOrderedMap extends ByteShortMap, OrderedMap<Byte, Short> {
    ByteShortMap.Entry firstEntry();

    boolean forFirst(@NotNull ByteShortBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(ByteShortBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Byte, ? super Short> action) {
        return this.forFirst((byte l_t, short l_v) -> action.accept(l_t, l_v));
    }

    ByteShortMap.Entry lastEntry();

    boolean forLast(@NotNull ByteShortBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(ByteShortBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Byte, ? super Short> action) {
        return this.forLast((byte l_t, short l_v) -> action.accept(l_t, l_v));
    }


    short putFirst(byte key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(byte, short)
     */
    @Deprecated
    default Short putFirst(Byte key, Short value) {
        return this.putFirst(key != null ? key.byteValue() : (byte) 0, value != null ? value.shortValue() : (short) 0);
    }

    short putLast(byte key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(byte, short)
     */
    @Deprecated
    default Short putLast(Byte key, Short value) {
        return this.putLast(key != null ? key.byteValue() : (byte) 0, value != null ? value.shortValue() : (short) 0);
    }

    ByteShortMap.Entry pollFirstEntry();

    boolean pollFirst(@NotNull ByteShortBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(ByteShortBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Byte, ? super Short> action) {
        return this.pollFirst((byte l_t, short l_v) -> action.accept(l_t, l_v));
    }

    ByteShortMap.Entry pollLastEntry();

    boolean pollLast(@NotNull ByteShortBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(ByteShortBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Byte, ? super Short> action) {
        return this.pollLast((byte l_t, short l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull ByteShortBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(ByteShortBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Byte, ? super Short> action) {
        this.forEachReversed((byte l_t, short l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    ByteOrderedSet keySet();

    @NotNull
    ShortOrderedCollection values();

    @NotNull
    OrderedEntrySet<ByteShortMap.Entry> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Byte, Short>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
