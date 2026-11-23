package io.github.shiozakanaptr.acext.api.collection.doubletype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.floattype.FloatOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.doubletype.DoubleFloatBiConsumer;
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
public interface DoubleFloatOrderedMap extends DoubleFloatMap, OrderedMap<Double, Float> {
    DoubleFloatMap.Entry firstEntry();

    boolean forFirst(@NotNull DoubleFloatBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(DoubleFloatBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Double, ? super Float> action) {
        return this.forFirst((double l_t, float l_v) -> action.accept(l_t, l_v));
    }

    DoubleFloatMap.Entry lastEntry();

    boolean forLast(@NotNull DoubleFloatBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(DoubleFloatBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Double, ? super Float> action) {
        return this.forLast((double l_t, float l_v) -> action.accept(l_t, l_v));
    }


    float putFirst(double key, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(double, float)
     */
    @Deprecated
    default Float putFirst(Double key, Float value) {
        return this.putFirst(key != null ? key.doubleValue() : (double) 0, value != null ? value.floatValue() : (float) 0);
    }

    float putLast(double key, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(double, float)
     */
    @Deprecated
    default Float putLast(Double key, Float value) {
        return this.putLast(key != null ? key.doubleValue() : (double) 0, value != null ? value.floatValue() : (float) 0);
    }

    DoubleFloatMap.Entry pollFirstEntry();

    boolean pollFirst(@NotNull DoubleFloatBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(DoubleFloatBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Double, ? super Float> action) {
        return this.pollFirst((double l_t, float l_v) -> action.accept(l_t, l_v));
    }

    DoubleFloatMap.Entry pollLastEntry();

    boolean pollLast(@NotNull DoubleFloatBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(DoubleFloatBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Double, ? super Float> action) {
        return this.pollLast((double l_t, float l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull DoubleFloatBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(DoubleFloatBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Double, ? super Float> action) {
        this.forEachReversed((double l_t, float l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    DoubleOrderedSet keySet();

    @NotNull
    FloatOrderedCollection values();

    @NotNull
    OrderedEntrySet<DoubleFloatMap.Entry> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Double, Float>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
