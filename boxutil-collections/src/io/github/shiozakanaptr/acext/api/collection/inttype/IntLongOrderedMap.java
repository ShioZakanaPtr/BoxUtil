package io.github.shiozakanaptr.acext.api.collection.inttype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.longtype.LongOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.inttype.IntLongBiConsumer;
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
public interface IntLongOrderedMap extends IntLongMap, OrderedMap<Integer, Long> {
    IntLongMap.Entry firstEntry();

    boolean forFirst(@NotNull IntLongBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(IntLongBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Integer, ? super Long> action) {
        return this.forFirst((int l_t, long l_v) -> action.accept(l_t, l_v));
    }

    IntLongMap.Entry lastEntry();

    boolean forLast(@NotNull IntLongBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(IntLongBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Integer, ? super Long> action) {
        return this.forLast((int l_t, long l_v) -> action.accept(l_t, l_v));
    }


    long putFirst(int key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(int, long)
     */
    @Deprecated
    default Long putFirst(Integer key, Long value) {
        return this.putFirst(key != null ? key.intValue() : (int) 0, value != null ? value.longValue() : (long) 0);
    }

    long putLast(int key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(int, long)
     */
    @Deprecated
    default Long putLast(Integer key, Long value) {
        return this.putLast(key != null ? key.intValue() : (int) 0, value != null ? value.longValue() : (long) 0);
    }

    IntLongMap.Entry pollFirstEntry();

    boolean pollFirst(@NotNull IntLongBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(IntLongBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Integer, ? super Long> action) {
        return this.pollFirst((int l_t, long l_v) -> action.accept(l_t, l_v));
    }

    IntLongMap.Entry pollLastEntry();

    boolean pollLast(@NotNull IntLongBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(IntLongBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Integer, ? super Long> action) {
        return this.pollLast((int l_t, long l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull IntLongBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(IntLongBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Integer, ? super Long> action) {
        this.forEachReversed((int l_t, long l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    IntOrderedSet keySet();

    @NotNull
    LongOrderedCollection values();

    @NotNull
    OrderedEntrySet<IntLongMap.Entry> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Integer, Long>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
