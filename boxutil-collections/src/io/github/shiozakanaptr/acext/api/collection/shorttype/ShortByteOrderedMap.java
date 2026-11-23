package io.github.shiozakanaptr.acext.api.collection.shorttype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.bytetype.ByteOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.shorttype.ShortByteBiConsumer;
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
public interface ShortByteOrderedMap extends ShortByteMap, OrderedMap<Short, Byte> {
    ShortByteMap.Entry firstEntry();

    boolean forFirst(@NotNull ShortByteBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(ShortByteBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Short, ? super Byte> action) {
        return this.forFirst((short l_t, byte l_v) -> action.accept(l_t, l_v));
    }

    ShortByteMap.Entry lastEntry();

    boolean forLast(@NotNull ShortByteBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(ShortByteBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Short, ? super Byte> action) {
        return this.forLast((short l_t, byte l_v) -> action.accept(l_t, l_v));
    }


    byte putFirst(short key, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(short, byte)
     */
    @Deprecated
    default Byte putFirst(Short key, Byte value) {
        return this.putFirst(key != null ? key.shortValue() : (short) 0, value != null ? value.byteValue() : (byte) 0);
    }

    byte putLast(short key, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(short, byte)
     */
    @Deprecated
    default Byte putLast(Short key, Byte value) {
        return this.putLast(key != null ? key.shortValue() : (short) 0, value != null ? value.byteValue() : (byte) 0);
    }

    ShortByteMap.Entry pollFirstEntry();

    boolean pollFirst(@NotNull ShortByteBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(ShortByteBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Short, ? super Byte> action) {
        return this.pollFirst((short l_t, byte l_v) -> action.accept(l_t, l_v));
    }

    ShortByteMap.Entry pollLastEntry();

    boolean pollLast(@NotNull ShortByteBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(ShortByteBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Short, ? super Byte> action) {
        return this.pollLast((short l_t, byte l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull ShortByteBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(ShortByteBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Short, ? super Byte> action) {
        this.forEachReversed((short l_t, byte l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    ShortOrderedSet keySet();

    @NotNull
    ByteOrderedCollection values();

    @NotNull
    OrderedEntrySet<ShortByteMap.Entry> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Short, Byte>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
