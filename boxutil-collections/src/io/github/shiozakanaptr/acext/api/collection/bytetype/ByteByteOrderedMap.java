package io.github.shiozakanaptr.acext.api.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.util.function.bytetype.ByteByteBiConsumer;
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
public interface ByteByteOrderedMap extends ByteByteMap, OrderedMap<Byte, Byte> {
    ByteByteMap.Entry firstEntry();

    boolean forFirst(@NotNull ByteByteBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(ByteByteBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Byte, ? super Byte> action) {
        return this.forFirst((byte l_t, byte l_v) -> action.accept(l_t, l_v));
    }

    ByteByteMap.Entry lastEntry();

    boolean forLast(@NotNull ByteByteBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(ByteByteBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Byte, ? super Byte> action) {
        return this.forLast((byte l_t, byte l_v) -> action.accept(l_t, l_v));
    }


    byte putFirst(byte key, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(byte, byte)
     */
    @Deprecated
    default Byte putFirst(Byte key, Byte value) {
        return this.putFirst(key != null ? key.byteValue() : (byte) 0, value != null ? value.byteValue() : (byte) 0);
    }

    byte putLast(byte key, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(byte, byte)
     */
    @Deprecated
    default Byte putLast(Byte key, Byte value) {
        return this.putLast(key != null ? key.byteValue() : (byte) 0, value != null ? value.byteValue() : (byte) 0);
    }

    ByteByteMap.Entry pollFirstEntry();

    boolean pollFirst(@NotNull ByteByteBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(ByteByteBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Byte, ? super Byte> action) {
        return this.pollFirst((byte l_t, byte l_v) -> action.accept(l_t, l_v));
    }

    ByteByteMap.Entry pollLastEntry();

    boolean pollLast(@NotNull ByteByteBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(ByteByteBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Byte, ? super Byte> action) {
        return this.pollLast((byte l_t, byte l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull ByteByteBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(ByteByteBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Byte, ? super Byte> action) {
        this.forEachReversed((byte l_t, byte l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    ByteOrderedSet keySet();

    @NotNull
    ByteOrderedCollection values();

    @NotNull
    OrderedEntrySet<ByteByteMap.Entry> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Byte, Byte>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
