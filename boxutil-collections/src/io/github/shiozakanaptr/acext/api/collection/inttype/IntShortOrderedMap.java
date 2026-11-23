package io.github.shiozakanaptr.acext.api.collection.inttype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.shorttype.ShortOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.inttype.IntShortBiConsumer;
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
public interface IntShortOrderedMap extends IntShortMap, OrderedMap<Integer, Short> {
    IntShortMap.Entry firstEntry();

    boolean forFirst(@NotNull IntShortBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(IntShortBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Integer, ? super Short> action) {
        return this.forFirst((int l_t, short l_v) -> action.accept(l_t, l_v));
    }

    IntShortMap.Entry lastEntry();

    boolean forLast(@NotNull IntShortBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(IntShortBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Integer, ? super Short> action) {
        return this.forLast((int l_t, short l_v) -> action.accept(l_t, l_v));
    }


    short putFirst(int key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(int, short)
     */
    @Deprecated
    default Short putFirst(Integer key, Short value) {
        return this.putFirst(key != null ? key.intValue() : (int) 0, value != null ? value.shortValue() : (short) 0);
    }

    short putLast(int key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(int, short)
     */
    @Deprecated
    default Short putLast(Integer key, Short value) {
        return this.putLast(key != null ? key.intValue() : (int) 0, value != null ? value.shortValue() : (short) 0);
    }

    IntShortMap.Entry pollFirstEntry();

    boolean pollFirst(@NotNull IntShortBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(IntShortBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Integer, ? super Short> action) {
        return this.pollFirst((int l_t, short l_v) -> action.accept(l_t, l_v));
    }

    IntShortMap.Entry pollLastEntry();

    boolean pollLast(@NotNull IntShortBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(IntShortBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Integer, ? super Short> action) {
        return this.pollLast((int l_t, short l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull IntShortBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(IntShortBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Integer, ? super Short> action) {
        this.forEachReversed((int l_t, short l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    IntOrderedSet keySet();

    @NotNull
    ShortOrderedCollection values();

    @NotNull
    OrderedEntrySet<IntShortMap.Entry> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Integer, Short>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
