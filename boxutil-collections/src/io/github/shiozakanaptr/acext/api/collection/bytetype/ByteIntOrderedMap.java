package io.github.shiozakanaptr.acext.api.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.inttype.IntOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.bytetype.ByteIntBiConsumer;
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
public interface ByteIntOrderedMap extends ByteIntMap, OrderedMap<Byte, Integer> {
    ByteIntMap.Entry firstEntry();

    boolean forFirst(@NotNull ByteIntBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(ByteIntBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Byte, ? super Integer> action) {
        return this.forFirst((byte l_t, int l_v) -> action.accept(l_t, l_v));
    }

    ByteIntMap.Entry lastEntry();

    boolean forLast(@NotNull ByteIntBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(ByteIntBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Byte, ? super Integer> action) {
        return this.forLast((byte l_t, int l_v) -> action.accept(l_t, l_v));
    }


    int putFirst(byte key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(byte, int)
     */
    @Deprecated
    default Integer putFirst(Byte key, Integer value) {
        return this.putFirst(key != null ? key.byteValue() : (byte) 0, value != null ? value.intValue() : (int) 0);
    }

    int putLast(byte key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(byte, int)
     */
    @Deprecated
    default Integer putLast(Byte key, Integer value) {
        return this.putLast(key != null ? key.byteValue() : (byte) 0, value != null ? value.intValue() : (int) 0);
    }

    ByteIntMap.Entry pollFirstEntry();

    boolean pollFirst(@NotNull ByteIntBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(ByteIntBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Byte, ? super Integer> action) {
        return this.pollFirst((byte l_t, int l_v) -> action.accept(l_t, l_v));
    }

    ByteIntMap.Entry pollLastEntry();

    boolean pollLast(@NotNull ByteIntBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(ByteIntBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Byte, ? super Integer> action) {
        return this.pollLast((byte l_t, int l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull ByteIntBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(ByteIntBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Byte, ? super Integer> action) {
        this.forEachReversed((byte l_t, int l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    ByteOrderedSet keySet();

    @NotNull
    IntOrderedCollection values();

    @NotNull
    OrderedEntrySet<ByteIntMap.Entry> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Byte, Integer>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
