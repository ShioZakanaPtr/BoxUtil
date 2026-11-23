package io.github.shiozakanaptr.acext.api.collection.longtype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.doubletype.DoubleOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.longtype.LongDoubleBiConsumer;
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
public interface LongDoubleOrderedMap extends LongDoubleMap, OrderedMap<Long, Double> {
    LongDoubleMap.Entry firstEntry();

    boolean forFirst(@NotNull LongDoubleBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(LongDoubleBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Long, ? super Double> action) {
        return this.forFirst((long l_t, double l_v) -> action.accept(l_t, l_v));
    }

    LongDoubleMap.Entry lastEntry();

    boolean forLast(@NotNull LongDoubleBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(LongDoubleBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Long, ? super Double> action) {
        return this.forLast((long l_t, double l_v) -> action.accept(l_t, l_v));
    }


    double putFirst(long key, double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(long, double)
     */
    @Deprecated
    default Double putFirst(Long key, Double value) {
        return this.putFirst(key != null ? key.longValue() : (long) 0, value != null ? value.doubleValue() : (double) 0);
    }

    double putLast(long key, double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(long, double)
     */
    @Deprecated
    default Double putLast(Long key, Double value) {
        return this.putLast(key != null ? key.longValue() : (long) 0, value != null ? value.doubleValue() : (double) 0);
    }

    LongDoubleMap.Entry pollFirstEntry();

    boolean pollFirst(@NotNull LongDoubleBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(LongDoubleBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Long, ? super Double> action) {
        return this.pollFirst((long l_t, double l_v) -> action.accept(l_t, l_v));
    }

    LongDoubleMap.Entry pollLastEntry();

    boolean pollLast(@NotNull LongDoubleBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(LongDoubleBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Long, ? super Double> action) {
        return this.pollLast((long l_t, double l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull LongDoubleBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(LongDoubleBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Long, ? super Double> action) {
        this.forEachReversed((long l_t, double l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    LongOrderedSet keySet();

    @NotNull
    DoubleOrderedCollection values();

    @NotNull
    OrderedEntrySet<LongDoubleMap.Entry> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Long, Double>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
