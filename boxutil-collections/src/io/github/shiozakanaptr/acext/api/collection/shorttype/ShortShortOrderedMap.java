package io.github.shiozakanaptr.acext.api.collection.shorttype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.util.function.shorttype.ShortShortBiConsumer;
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
public interface ShortShortOrderedMap extends ShortShortMap, OrderedMap<Short, Short> {
    ShortShortMap.Entry firstEntry();

    boolean forFirst(@NotNull ShortShortBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(ShortShortBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Short, ? super Short> action) {
        return this.forFirst((short l_t, short l_v) -> action.accept(l_t, l_v));
    }

    ShortShortMap.Entry lastEntry();

    boolean forLast(@NotNull ShortShortBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(ShortShortBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Short, ? super Short> action) {
        return this.forLast((short l_t, short l_v) -> action.accept(l_t, l_v));
    }


    short putFirst(short key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(short, short)
     */
    @Deprecated
    default Short putFirst(Short key, Short value) {
        return this.putFirst(key != null ? key.shortValue() : (short) 0, value != null ? value.shortValue() : (short) 0);
    }

    short putLast(short key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(short, short)
     */
    @Deprecated
    default Short putLast(Short key, Short value) {
        return this.putLast(key != null ? key.shortValue() : (short) 0, value != null ? value.shortValue() : (short) 0);
    }

    ShortShortMap.Entry pollFirstEntry();

    boolean pollFirst(@NotNull ShortShortBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(ShortShortBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Short, ? super Short> action) {
        return this.pollFirst((short l_t, short l_v) -> action.accept(l_t, l_v));
    }

    ShortShortMap.Entry pollLastEntry();

    boolean pollLast(@NotNull ShortShortBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(ShortShortBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Short, ? super Short> action) {
        return this.pollLast((short l_t, short l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull ShortShortBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(ShortShortBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Short, ? super Short> action) {
        this.forEachReversed((short l_t, short l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    ShortOrderedSet keySet();

    @NotNull
    ShortOrderedCollection values();

    @NotNull
    OrderedEntrySet<ShortShortMap.Entry> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Short, Short>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
