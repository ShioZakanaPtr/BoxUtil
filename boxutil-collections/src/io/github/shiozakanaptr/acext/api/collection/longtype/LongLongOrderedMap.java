package io.github.shiozakanaptr.acext.api.collection.longtype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.util.function.longtype.LongLongBiConsumer;
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
public interface LongLongOrderedMap extends LongLongMap, OrderedMap<Long, Long> {
    LongLongMap.Entry firstEntry();

    boolean forFirst(@NotNull LongLongBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(LongLongBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Long, ? super Long> action) {
        return this.forFirst((long l_t, long l_v) -> action.accept(l_t, l_v));
    }

    LongLongMap.Entry lastEntry();

    boolean forLast(@NotNull LongLongBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(LongLongBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Long, ? super Long> action) {
        return this.forLast((long l_t, long l_v) -> action.accept(l_t, l_v));
    }


    long putFirst(long key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(long, long)
     */
    @Deprecated
    default Long putFirst(Long key, Long value) {
        return this.putFirst(key != null ? key.longValue() : (long) 0, value != null ? value.longValue() : (long) 0);
    }

    long putLast(long key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(long, long)
     */
    @Deprecated
    default Long putLast(Long key, Long value) {
        return this.putLast(key != null ? key.longValue() : (long) 0, value != null ? value.longValue() : (long) 0);
    }

    LongLongMap.Entry pollFirstEntry();

    boolean pollFirst(@NotNull LongLongBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(LongLongBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Long, ? super Long> action) {
        return this.pollFirst((long l_t, long l_v) -> action.accept(l_t, l_v));
    }

    LongLongMap.Entry pollLastEntry();

    boolean pollLast(@NotNull LongLongBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(LongLongBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Long, ? super Long> action) {
        return this.pollLast((long l_t, long l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull LongLongBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(LongLongBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Long, ? super Long> action) {
        this.forEachReversed((long l_t, long l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    LongOrderedSet keySet();

    @NotNull
    LongOrderedCollection values();

    @NotNull
    OrderedEntrySet<LongLongMap.Entry> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Long, Long>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
