package io.github.shiozakanaptr.acext.api.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.doubletype.DoubleOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.bytetype.ByteDoubleBiConsumer;
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
public interface ByteDoubleOrderedMap extends ByteDoubleMap, OrderedMap<Byte, Double> {
    ByteDoubleMap.Entry firstEntry();

    boolean forFirst(@NotNull ByteDoubleBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(ByteDoubleBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Byte, ? super Double> action) {
        return this.forFirst((byte l_t, double l_v) -> action.accept(l_t, l_v));
    }

    ByteDoubleMap.Entry lastEntry();

    boolean forLast(@NotNull ByteDoubleBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(ByteDoubleBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Byte, ? super Double> action) {
        return this.forLast((byte l_t, double l_v) -> action.accept(l_t, l_v));
    }


    double putFirst(byte key, double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(byte, double)
     */
    @Deprecated
    default Double putFirst(Byte key, Double value) {
        return this.putFirst(key != null ? key.byteValue() : (byte) 0, value != null ? value.doubleValue() : (double) 0);
    }

    double putLast(byte key, double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(byte, double)
     */
    @Deprecated
    default Double putLast(Byte key, Double value) {
        return this.putLast(key != null ? key.byteValue() : (byte) 0, value != null ? value.doubleValue() : (double) 0);
    }

    ByteDoubleMap.Entry pollFirstEntry();

    boolean pollFirst(@NotNull ByteDoubleBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(ByteDoubleBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Byte, ? super Double> action) {
        return this.pollFirst((byte l_t, double l_v) -> action.accept(l_t, l_v));
    }

    ByteDoubleMap.Entry pollLastEntry();

    boolean pollLast(@NotNull ByteDoubleBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(ByteDoubleBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Byte, ? super Double> action) {
        return this.pollLast((byte l_t, double l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull ByteDoubleBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(ByteDoubleBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Byte, ? super Double> action) {
        this.forEachReversed((byte l_t, double l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    ByteOrderedSet keySet();

    @NotNull
    DoubleOrderedCollection values();

    @NotNull
    OrderedEntrySet<ByteDoubleMap.Entry> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Byte, Double>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
