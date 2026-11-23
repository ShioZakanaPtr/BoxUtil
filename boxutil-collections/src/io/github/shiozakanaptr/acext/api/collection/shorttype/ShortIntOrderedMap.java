package io.github.shiozakanaptr.acext.api.collection.shorttype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.inttype.IntOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.shorttype.ShortIntBiConsumer;
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
public interface ShortIntOrderedMap extends ShortIntMap, OrderedMap<Short, Integer> {
    ShortIntMap.Entry firstEntry();

    boolean forFirst(@NotNull ShortIntBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(ShortIntBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Short, ? super Integer> action) {
        return this.forFirst((short l_t, int l_v) -> action.accept(l_t, l_v));
    }

    ShortIntMap.Entry lastEntry();

    boolean forLast(@NotNull ShortIntBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(ShortIntBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Short, ? super Integer> action) {
        return this.forLast((short l_t, int l_v) -> action.accept(l_t, l_v));
    }


    int putFirst(short key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(short, int)
     */
    @Deprecated
    default Integer putFirst(Short key, Integer value) {
        return this.putFirst(key != null ? key.shortValue() : (short) 0, value != null ? value.intValue() : (int) 0);
    }

    int putLast(short key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(short, int)
     */
    @Deprecated
    default Integer putLast(Short key, Integer value) {
        return this.putLast(key != null ? key.shortValue() : (short) 0, value != null ? value.intValue() : (int) 0);
    }

    ShortIntMap.Entry pollFirstEntry();

    boolean pollFirst(@NotNull ShortIntBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(ShortIntBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Short, ? super Integer> action) {
        return this.pollFirst((short l_t, int l_v) -> action.accept(l_t, l_v));
    }

    ShortIntMap.Entry pollLastEntry();

    boolean pollLast(@NotNull ShortIntBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(ShortIntBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Short, ? super Integer> action) {
        return this.pollLast((short l_t, int l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull ShortIntBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(ShortIntBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Short, ? super Integer> action) {
        this.forEachReversed((short l_t, int l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    ShortOrderedSet keySet();

    @NotNull
    IntOrderedCollection values();

    @NotNull
    OrderedEntrySet<ShortIntMap.Entry> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Short, Integer>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
