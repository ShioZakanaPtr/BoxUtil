package io.github.shiozakanaptr.acext.api.collection.shorttype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.longtype.LongOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.shorttype.ShortLongBiConsumer;
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
public interface ShortLongOrderedMap extends ShortLongMap, OrderedMap<Short, Long> {
    ShortLongMap.Entry firstEntry();

    boolean forFirst(@NotNull ShortLongBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(ShortLongBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Short, ? super Long> action) {
        return this.forFirst((short l_t, long l_v) -> action.accept(l_t, l_v));
    }

    ShortLongMap.Entry lastEntry();

    boolean forLast(@NotNull ShortLongBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(ShortLongBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Short, ? super Long> action) {
        return this.forLast((short l_t, long l_v) -> action.accept(l_t, l_v));
    }


    long putFirst(short key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(short, long)
     */
    @Deprecated
    default Long putFirst(Short key, Long value) {
        return this.putFirst(key != null ? key.shortValue() : (short) 0, value != null ? value.longValue() : (long) 0);
    }

    long putLast(short key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(short, long)
     */
    @Deprecated
    default Long putLast(Short key, Long value) {
        return this.putLast(key != null ? key.shortValue() : (short) 0, value != null ? value.longValue() : (long) 0);
    }

    ShortLongMap.Entry pollFirstEntry();

    boolean pollFirst(@NotNull ShortLongBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(ShortLongBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Short, ? super Long> action) {
        return this.pollFirst((short l_t, long l_v) -> action.accept(l_t, l_v));
    }

    ShortLongMap.Entry pollLastEntry();

    boolean pollLast(@NotNull ShortLongBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(ShortLongBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Short, ? super Long> action) {
        return this.pollLast((short l_t, long l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull ShortLongBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(ShortLongBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Short, ? super Long> action) {
        this.forEachReversed((short l_t, long l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    ShortOrderedSet keySet();

    @NotNull
    LongOrderedCollection values();

    @NotNull
    OrderedEntrySet<ShortLongMap.Entry> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Short, Long>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
