package io.github.shiozakanaptr.acext.api.collection.inttype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.bytetype.ByteOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.inttype.IntByteBiConsumer;
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
public interface IntByteOrderedMap extends IntByteMap, OrderedMap<Integer, Byte> {
    IntByteMap.Entry firstEntry();

    boolean forFirst(@NotNull IntByteBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(IntByteBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Integer, ? super Byte> action) {
        return this.forFirst((int l_t, byte l_v) -> action.accept(l_t, l_v));
    }

    IntByteMap.Entry lastEntry();

    boolean forLast(@NotNull IntByteBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(IntByteBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Integer, ? super Byte> action) {
        return this.forLast((int l_t, byte l_v) -> action.accept(l_t, l_v));
    }


    byte putFirst(int key, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(int, byte)
     */
    @Deprecated
    default Byte putFirst(Integer key, Byte value) {
        return this.putFirst(key != null ? key.intValue() : (int) 0, value != null ? value.byteValue() : (byte) 0);
    }

    byte putLast(int key, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(int, byte)
     */
    @Deprecated
    default Byte putLast(Integer key, Byte value) {
        return this.putLast(key != null ? key.intValue() : (int) 0, value != null ? value.byteValue() : (byte) 0);
    }

    IntByteMap.Entry pollFirstEntry();

    boolean pollFirst(@NotNull IntByteBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(IntByteBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Integer, ? super Byte> action) {
        return this.pollFirst((int l_t, byte l_v) -> action.accept(l_t, l_v));
    }

    IntByteMap.Entry pollLastEntry();

    boolean pollLast(@NotNull IntByteBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(IntByteBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Integer, ? super Byte> action) {
        return this.pollLast((int l_t, byte l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull IntByteBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(IntByteBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Integer, ? super Byte> action) {
        this.forEachReversed((int l_t, byte l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    IntOrderedSet keySet();

    @NotNull
    ByteOrderedCollection values();

    @NotNull
    OrderedEntrySet<IntByteMap.Entry> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Integer, Byte>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
