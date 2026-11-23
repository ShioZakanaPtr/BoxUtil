package io.github.shiozakanaptr.acext.api.collection.longtype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.shorttype.ShortOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.longtype.LongShortBiConsumer;
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
public interface LongShortOrderedMap extends LongShortMap, OrderedMap<Long, Short> {
    LongShortMap.Entry firstEntry();

    boolean forFirst(@NotNull LongShortBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(LongShortBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Long, ? super Short> action) {
        return this.forFirst((long l_t, short l_v) -> action.accept(l_t, l_v));
    }

    LongShortMap.Entry lastEntry();

    boolean forLast(@NotNull LongShortBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(LongShortBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Long, ? super Short> action) {
        return this.forLast((long l_t, short l_v) -> action.accept(l_t, l_v));
    }


    short putFirst(long key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(long, short)
     */
    @Deprecated
    default Short putFirst(Long key, Short value) {
        return this.putFirst(key != null ? key.longValue() : (long) 0, value != null ? value.shortValue() : (short) 0);
    }

    short putLast(long key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(long, short)
     */
    @Deprecated
    default Short putLast(Long key, Short value) {
        return this.putLast(key != null ? key.longValue() : (long) 0, value != null ? value.shortValue() : (short) 0);
    }

    LongShortMap.Entry pollFirstEntry();

    boolean pollFirst(@NotNull LongShortBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(LongShortBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Long, ? super Short> action) {
        return this.pollFirst((long l_t, short l_v) -> action.accept(l_t, l_v));
    }

    LongShortMap.Entry pollLastEntry();

    boolean pollLast(@NotNull LongShortBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(LongShortBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Long, ? super Short> action) {
        return this.pollLast((long l_t, short l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull LongShortBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(LongShortBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Long, ? super Short> action) {
        this.forEachReversed((long l_t, short l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    LongOrderedSet keySet();

    @NotNull
    ShortOrderedCollection values();

    @NotNull
    OrderedEntrySet<LongShortMap.Entry> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Long, Short>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
