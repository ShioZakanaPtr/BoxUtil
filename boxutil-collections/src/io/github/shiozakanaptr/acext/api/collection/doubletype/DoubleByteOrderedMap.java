package io.github.shiozakanaptr.acext.api.collection.doubletype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.bytetype.ByteOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.doubletype.DoubleByteBiConsumer;
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
public interface DoubleByteOrderedMap extends DoubleByteMap, OrderedMap<Double, Byte> {
    DoubleByteMap.Entry firstEntry();

    boolean forFirst(@NotNull DoubleByteBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(DoubleByteBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Double, ? super Byte> action) {
        return this.forFirst((double l_t, byte l_v) -> action.accept(l_t, l_v));
    }

    DoubleByteMap.Entry lastEntry();

    boolean forLast(@NotNull DoubleByteBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(DoubleByteBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Double, ? super Byte> action) {
        return this.forLast((double l_t, byte l_v) -> action.accept(l_t, l_v));
    }


    byte putFirst(double key, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(double, byte)
     */
    @Deprecated
    default Byte putFirst(Double key, Byte value) {
        return this.putFirst(key != null ? key.doubleValue() : (double) 0, value != null ? value.byteValue() : (byte) 0);
    }

    byte putLast(double key, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(double, byte)
     */
    @Deprecated
    default Byte putLast(Double key, Byte value) {
        return this.putLast(key != null ? key.doubleValue() : (double) 0, value != null ? value.byteValue() : (byte) 0);
    }

    DoubleByteMap.Entry pollFirstEntry();

    boolean pollFirst(@NotNull DoubleByteBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(DoubleByteBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Double, ? super Byte> action) {
        return this.pollFirst((double l_t, byte l_v) -> action.accept(l_t, l_v));
    }

    DoubleByteMap.Entry pollLastEntry();

    boolean pollLast(@NotNull DoubleByteBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(DoubleByteBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Double, ? super Byte> action) {
        return this.pollLast((double l_t, byte l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull DoubleByteBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(DoubleByteBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Double, ? super Byte> action) {
        this.forEachReversed((double l_t, byte l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    DoubleOrderedSet keySet();

    @NotNull
    ByteOrderedCollection values();

    @NotNull
    OrderedEntrySet<DoubleByteMap.Entry> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Double, Byte>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
