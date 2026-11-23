package io.github.shiozakanaptr.acext.api.collection.longtype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.inttype.IntOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.longtype.LongIntBiConsumer;
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
public interface LongIntOrderedMap extends LongIntMap, OrderedMap<Long, Integer> {
    LongIntMap.Entry firstEntry();

    boolean forFirst(@NotNull LongIntBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(LongIntBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Long, ? super Integer> action) {
        return this.forFirst((long l_t, int l_v) -> action.accept(l_t, l_v));
    }

    LongIntMap.Entry lastEntry();

    boolean forLast(@NotNull LongIntBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(LongIntBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Long, ? super Integer> action) {
        return this.forLast((long l_t, int l_v) -> action.accept(l_t, l_v));
    }


    int putFirst(long key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(long, int)
     */
    @Deprecated
    default Integer putFirst(Long key, Integer value) {
        return this.putFirst(key != null ? key.longValue() : (long) 0, value != null ? value.intValue() : (int) 0);
    }

    int putLast(long key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(long, int)
     */
    @Deprecated
    default Integer putLast(Long key, Integer value) {
        return this.putLast(key != null ? key.longValue() : (long) 0, value != null ? value.intValue() : (int) 0);
    }

    LongIntMap.Entry pollFirstEntry();

    boolean pollFirst(@NotNull LongIntBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(LongIntBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Long, ? super Integer> action) {
        return this.pollFirst((long l_t, int l_v) -> action.accept(l_t, l_v));
    }

    LongIntMap.Entry pollLastEntry();

    boolean pollLast(@NotNull LongIntBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(LongIntBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Long, ? super Integer> action) {
        return this.pollLast((long l_t, int l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull LongIntBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(LongIntBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Long, ? super Integer> action) {
        this.forEachReversed((long l_t, int l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    LongOrderedSet keySet();

    @NotNull
    IntOrderedCollection values();

    @NotNull
    OrderedEntrySet<LongIntMap.Entry> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Long, Integer>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
