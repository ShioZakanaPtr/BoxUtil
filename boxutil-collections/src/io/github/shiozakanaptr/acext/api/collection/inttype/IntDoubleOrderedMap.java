package io.github.shiozakanaptr.acext.api.collection.inttype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.doubletype.DoubleOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.inttype.IntDoubleBiConsumer;
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
public interface IntDoubleOrderedMap extends IntDoubleMap, OrderedMap<Integer, Double> {
    IntDoubleMap.Entry firstEntry();

    boolean forFirst(@NotNull IntDoubleBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(IntDoubleBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Integer, ? super Double> action) {
        return this.forFirst((int l_t, double l_v) -> action.accept(l_t, l_v));
    }

    IntDoubleMap.Entry lastEntry();

    boolean forLast(@NotNull IntDoubleBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(IntDoubleBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Integer, ? super Double> action) {
        return this.forLast((int l_t, double l_v) -> action.accept(l_t, l_v));
    }


    double putFirst(int key, double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(int, double)
     */
    @Deprecated
    default Double putFirst(Integer key, Double value) {
        return this.putFirst(key != null ? key.intValue() : (int) 0, value != null ? value.doubleValue() : (double) 0);
    }

    double putLast(int key, double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(int, double)
     */
    @Deprecated
    default Double putLast(Integer key, Double value) {
        return this.putLast(key != null ? key.intValue() : (int) 0, value != null ? value.doubleValue() : (double) 0);
    }

    IntDoubleMap.Entry pollFirstEntry();

    boolean pollFirst(@NotNull IntDoubleBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(IntDoubleBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Integer, ? super Double> action) {
        return this.pollFirst((int l_t, double l_v) -> action.accept(l_t, l_v));
    }

    IntDoubleMap.Entry pollLastEntry();

    boolean pollLast(@NotNull IntDoubleBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(IntDoubleBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Integer, ? super Double> action) {
        return this.pollLast((int l_t, double l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull IntDoubleBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(IntDoubleBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Integer, ? super Double> action) {
        this.forEachReversed((int l_t, double l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    IntOrderedSet keySet();

    @NotNull
    DoubleOrderedCollection values();

    @NotNull
    OrderedEntrySet<IntDoubleMap.Entry> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Integer, Double>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
