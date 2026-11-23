package io.github.shiozakanaptr.acext.api.collection.doubletype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.longtype.LongOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.doubletype.DoubleLongBiConsumer;
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
public interface DoubleLongOrderedMap extends DoubleLongMap, OrderedMap<Double, Long> {
    DoubleLongMap.Entry firstEntry();

    boolean forFirst(@NotNull DoubleLongBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(DoubleLongBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Double, ? super Long> action) {
        return this.forFirst((double l_t, long l_v) -> action.accept(l_t, l_v));
    }

    DoubleLongMap.Entry lastEntry();

    boolean forLast(@NotNull DoubleLongBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(DoubleLongBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Double, ? super Long> action) {
        return this.forLast((double l_t, long l_v) -> action.accept(l_t, l_v));
    }


    long putFirst(double key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(double, long)
     */
    @Deprecated
    default Long putFirst(Double key, Long value) {
        return this.putFirst(key != null ? key.doubleValue() : (double) 0, value != null ? value.longValue() : (long) 0);
    }

    long putLast(double key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(double, long)
     */
    @Deprecated
    default Long putLast(Double key, Long value) {
        return this.putLast(key != null ? key.doubleValue() : (double) 0, value != null ? value.longValue() : (long) 0);
    }

    DoubleLongMap.Entry pollFirstEntry();

    boolean pollFirst(@NotNull DoubleLongBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(DoubleLongBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Double, ? super Long> action) {
        return this.pollFirst((double l_t, long l_v) -> action.accept(l_t, l_v));
    }

    DoubleLongMap.Entry pollLastEntry();

    boolean pollLast(@NotNull DoubleLongBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(DoubleLongBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Double, ? super Long> action) {
        return this.pollLast((double l_t, long l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull DoubleLongBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(DoubleLongBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Double, ? super Long> action) {
        this.forEachReversed((double l_t, long l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    DoubleOrderedSet keySet();

    @NotNull
    LongOrderedCollection values();

    @NotNull
    OrderedEntrySet<DoubleLongMap.Entry> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Double, Long>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
