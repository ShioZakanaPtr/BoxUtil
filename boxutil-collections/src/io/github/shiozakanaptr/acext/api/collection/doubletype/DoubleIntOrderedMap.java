package io.github.shiozakanaptr.acext.api.collection.doubletype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.inttype.IntOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.doubletype.DoubleIntBiConsumer;
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
public interface DoubleIntOrderedMap extends DoubleIntMap, OrderedMap<Double, Integer> {
    DoubleIntMap.Entry firstEntry();

    boolean forFirst(@NotNull DoubleIntBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(DoubleIntBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Double, ? super Integer> action) {
        return this.forFirst((double l_t, int l_v) -> action.accept(l_t, l_v));
    }

    DoubleIntMap.Entry lastEntry();

    boolean forLast(@NotNull DoubleIntBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(DoubleIntBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Double, ? super Integer> action) {
        return this.forLast((double l_t, int l_v) -> action.accept(l_t, l_v));
    }


    int putFirst(double key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(double, int)
     */
    @Deprecated
    default Integer putFirst(Double key, Integer value) {
        return this.putFirst(key != null ? key.doubleValue() : (double) 0, value != null ? value.intValue() : (int) 0);
    }

    int putLast(double key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(double, int)
     */
    @Deprecated
    default Integer putLast(Double key, Integer value) {
        return this.putLast(key != null ? key.doubleValue() : (double) 0, value != null ? value.intValue() : (int) 0);
    }

    DoubleIntMap.Entry pollFirstEntry();

    boolean pollFirst(@NotNull DoubleIntBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(DoubleIntBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Double, ? super Integer> action) {
        return this.pollFirst((double l_t, int l_v) -> action.accept(l_t, l_v));
    }

    DoubleIntMap.Entry pollLastEntry();

    boolean pollLast(@NotNull DoubleIntBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(DoubleIntBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Double, ? super Integer> action) {
        return this.pollLast((double l_t, int l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull DoubleIntBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(DoubleIntBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Double, ? super Integer> action) {
        this.forEachReversed((double l_t, int l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    DoubleOrderedSet keySet();

    @NotNull
    IntOrderedCollection values();

    @NotNull
    OrderedEntrySet<DoubleIntMap.Entry> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Double, Integer>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
