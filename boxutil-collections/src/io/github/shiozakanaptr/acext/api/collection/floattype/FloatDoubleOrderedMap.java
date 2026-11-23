package io.github.shiozakanaptr.acext.api.collection.floattype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.doubletype.DoubleOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.floattype.FloatDoubleBiConsumer;
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
public interface FloatDoubleOrderedMap extends FloatDoubleMap, OrderedMap<Float, Double> {
    FloatDoubleMap.Entry firstEntry();

    boolean forFirst(@NotNull FloatDoubleBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(FloatDoubleBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Float, ? super Double> action) {
        return this.forFirst((float l_t, double l_v) -> action.accept(l_t, l_v));
    }

    FloatDoubleMap.Entry lastEntry();

    boolean forLast(@NotNull FloatDoubleBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(FloatDoubleBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Float, ? super Double> action) {
        return this.forLast((float l_t, double l_v) -> action.accept(l_t, l_v));
    }


    double putFirst(float key, double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(float, double)
     */
    @Deprecated
    default Double putFirst(Float key, Double value) {
        return this.putFirst(key != null ? key.floatValue() : (float) 0, value != null ? value.doubleValue() : (double) 0);
    }

    double putLast(float key, double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(float, double)
     */
    @Deprecated
    default Double putLast(Float key, Double value) {
        return this.putLast(key != null ? key.floatValue() : (float) 0, value != null ? value.doubleValue() : (double) 0);
    }

    FloatDoubleMap.Entry pollFirstEntry();

    boolean pollFirst(@NotNull FloatDoubleBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(FloatDoubleBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Float, ? super Double> action) {
        return this.pollFirst((float l_t, double l_v) -> action.accept(l_t, l_v));
    }

    FloatDoubleMap.Entry pollLastEntry();

    boolean pollLast(@NotNull FloatDoubleBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(FloatDoubleBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Float, ? super Double> action) {
        return this.pollLast((float l_t, double l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull FloatDoubleBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(FloatDoubleBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Float, ? super Double> action) {
        this.forEachReversed((float l_t, double l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    FloatOrderedSet keySet();

    @NotNull
    DoubleOrderedCollection values();

    @NotNull
    OrderedEntrySet<FloatDoubleMap.Entry> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Float, Double>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
