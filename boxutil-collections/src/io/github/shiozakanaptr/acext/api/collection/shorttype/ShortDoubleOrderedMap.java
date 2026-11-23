package io.github.shiozakanaptr.acext.api.collection.shorttype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.doubletype.DoubleOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.shorttype.ShortDoubleBiConsumer;
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
public interface ShortDoubleOrderedMap extends ShortDoubleMap, OrderedMap<Short, Double> {
    ShortDoubleMap.Entry firstEntry();

    boolean forFirst(@NotNull ShortDoubleBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(ShortDoubleBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Short, ? super Double> action) {
        return this.forFirst((short l_t, double l_v) -> action.accept(l_t, l_v));
    }

    ShortDoubleMap.Entry lastEntry();

    boolean forLast(@NotNull ShortDoubleBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(ShortDoubleBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Short, ? super Double> action) {
        return this.forLast((short l_t, double l_v) -> action.accept(l_t, l_v));
    }


    double putFirst(short key, double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(short, double)
     */
    @Deprecated
    default Double putFirst(Short key, Double value) {
        return this.putFirst(key != null ? key.shortValue() : (short) 0, value != null ? value.doubleValue() : (double) 0);
    }

    double putLast(short key, double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(short, double)
     */
    @Deprecated
    default Double putLast(Short key, Double value) {
        return this.putLast(key != null ? key.shortValue() : (short) 0, value != null ? value.doubleValue() : (double) 0);
    }

    ShortDoubleMap.Entry pollFirstEntry();

    boolean pollFirst(@NotNull ShortDoubleBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(ShortDoubleBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Short, ? super Double> action) {
        return this.pollFirst((short l_t, double l_v) -> action.accept(l_t, l_v));
    }

    ShortDoubleMap.Entry pollLastEntry();

    boolean pollLast(@NotNull ShortDoubleBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(ShortDoubleBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Short, ? super Double> action) {
        return this.pollLast((short l_t, double l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull ShortDoubleBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(ShortDoubleBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Short, ? super Double> action) {
        this.forEachReversed((short l_t, double l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    ShortOrderedSet keySet();

    @NotNull
    DoubleOrderedCollection values();

    @NotNull
    OrderedEntrySet<ShortDoubleMap.Entry> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Short, Double>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
