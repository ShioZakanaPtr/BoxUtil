package io.github.shiozakanaptr.acext.api.collection.doubletype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.util.function.doubletype.DoubleDoubleBiConsumer;
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
public interface DoubleDoubleOrderedMap extends DoubleDoubleMap, OrderedMap<Double, Double> {
    DoubleDoubleMap.Entry firstEntry();

    boolean forFirst(@NotNull DoubleDoubleBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(DoubleDoubleBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Double, ? super Double> action) {
        return this.forFirst((double l_t, double l_v) -> action.accept(l_t, l_v));
    }

    DoubleDoubleMap.Entry lastEntry();

    boolean forLast(@NotNull DoubleDoubleBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(DoubleDoubleBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Double, ? super Double> action) {
        return this.forLast((double l_t, double l_v) -> action.accept(l_t, l_v));
    }


    double putFirst(double key, double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(double, double)
     */
    @Deprecated
    default Double putFirst(Double key, Double value) {
        return this.putFirst(key != null ? key.doubleValue() : (double) 0, value != null ? value.doubleValue() : (double) 0);
    }

    double putLast(double key, double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(double, double)
     */
    @Deprecated
    default Double putLast(Double key, Double value) {
        return this.putLast(key != null ? key.doubleValue() : (double) 0, value != null ? value.doubleValue() : (double) 0);
    }

    DoubleDoubleMap.Entry pollFirstEntry();

    boolean pollFirst(@NotNull DoubleDoubleBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(DoubleDoubleBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Double, ? super Double> action) {
        return this.pollFirst((double l_t, double l_v) -> action.accept(l_t, l_v));
    }

    DoubleDoubleMap.Entry pollLastEntry();

    boolean pollLast(@NotNull DoubleDoubleBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(DoubleDoubleBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Double, ? super Double> action) {
        return this.pollLast((double l_t, double l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull DoubleDoubleBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(DoubleDoubleBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Double, ? super Double> action) {
        this.forEachReversed((double l_t, double l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    DoubleOrderedSet keySet();

    @NotNull
    DoubleOrderedCollection values();

    @NotNull
    OrderedEntrySet<DoubleDoubleMap.Entry> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Double, Double>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
