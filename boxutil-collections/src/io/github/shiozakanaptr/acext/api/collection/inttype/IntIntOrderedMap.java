package io.github.shiozakanaptr.acext.api.collection.inttype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.util.function.inttype.IntIntBiConsumer;
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
public interface IntIntOrderedMap extends IntIntMap, OrderedMap<Integer, Integer> {
    IntIntMap.Entry firstEntry();

    boolean forFirst(@NotNull IntIntBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(IntIntBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Integer, ? super Integer> action) {
        return this.forFirst((int l_t, int l_v) -> action.accept(l_t, l_v));
    }

    IntIntMap.Entry lastEntry();

    boolean forLast(@NotNull IntIntBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(IntIntBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Integer, ? super Integer> action) {
        return this.forLast((int l_t, int l_v) -> action.accept(l_t, l_v));
    }


    int putFirst(int key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(int, int)
     */
    @Deprecated
    default Integer putFirst(Integer key, Integer value) {
        return this.putFirst(key != null ? key.intValue() : (int) 0, value != null ? value.intValue() : (int) 0);
    }

    int putLast(int key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(int, int)
     */
    @Deprecated
    default Integer putLast(Integer key, Integer value) {
        return this.putLast(key != null ? key.intValue() : (int) 0, value != null ? value.intValue() : (int) 0);
    }

    IntIntMap.Entry pollFirstEntry();

    boolean pollFirst(@NotNull IntIntBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(IntIntBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Integer, ? super Integer> action) {
        return this.pollFirst((int l_t, int l_v) -> action.accept(l_t, l_v));
    }

    IntIntMap.Entry pollLastEntry();

    boolean pollLast(@NotNull IntIntBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(IntIntBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Integer, ? super Integer> action) {
        return this.pollLast((int l_t, int l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull IntIntBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(IntIntBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Integer, ? super Integer> action) {
        this.forEachReversed((int l_t, int l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    IntOrderedSet keySet();

    @NotNull
    IntOrderedCollection values();

    @NotNull
    OrderedEntrySet<IntIntMap.Entry> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Integer, Integer>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
