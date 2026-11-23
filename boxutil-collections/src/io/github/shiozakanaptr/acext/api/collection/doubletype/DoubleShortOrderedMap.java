package io.github.shiozakanaptr.acext.api.collection.doubletype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.shorttype.ShortOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.doubletype.DoubleShortBiConsumer;
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
public interface DoubleShortOrderedMap extends DoubleShortMap, OrderedMap<Double, Short> {
    DoubleShortMap.Entry firstEntry();

    boolean forFirst(@NotNull DoubleShortBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(DoubleShortBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Double, ? super Short> action) {
        return this.forFirst((double l_t, short l_v) -> action.accept(l_t, l_v));
    }

    DoubleShortMap.Entry lastEntry();

    boolean forLast(@NotNull DoubleShortBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(DoubleShortBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Double, ? super Short> action) {
        return this.forLast((double l_t, short l_v) -> action.accept(l_t, l_v));
    }


    short putFirst(double key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(double, short)
     */
    @Deprecated
    default Short putFirst(Double key, Short value) {
        return this.putFirst(key != null ? key.doubleValue() : (double) 0, value != null ? value.shortValue() : (short) 0);
    }

    short putLast(double key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(double, short)
     */
    @Deprecated
    default Short putLast(Double key, Short value) {
        return this.putLast(key != null ? key.doubleValue() : (double) 0, value != null ? value.shortValue() : (short) 0);
    }

    DoubleShortMap.Entry pollFirstEntry();

    boolean pollFirst(@NotNull DoubleShortBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(DoubleShortBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Double, ? super Short> action) {
        return this.pollFirst((double l_t, short l_v) -> action.accept(l_t, l_v));
    }

    DoubleShortMap.Entry pollLastEntry();

    boolean pollLast(@NotNull DoubleShortBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(DoubleShortBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Double, ? super Short> action) {
        return this.pollLast((double l_t, short l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull DoubleShortBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(DoubleShortBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Double, ? super Short> action) {
        this.forEachReversed((double l_t, short l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    DoubleOrderedSet keySet();

    @NotNull
    ShortOrderedCollection values();

    @NotNull
    OrderedEntrySet<DoubleShortMap.Entry> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Double, Short>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
