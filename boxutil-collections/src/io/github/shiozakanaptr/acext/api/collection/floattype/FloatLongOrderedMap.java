package io.github.shiozakanaptr.acext.api.collection.floattype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.longtype.LongOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.floattype.FloatLongBiConsumer;
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
public interface FloatLongOrderedMap extends FloatLongMap, OrderedMap<Float, Long> {
    FloatLongMap.Entry firstEntry();

    boolean forFirst(@NotNull FloatLongBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(FloatLongBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Float, ? super Long> action) {
        return this.forFirst((float l_t, long l_v) -> action.accept(l_t, l_v));
    }

    FloatLongMap.Entry lastEntry();

    boolean forLast(@NotNull FloatLongBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(FloatLongBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Float, ? super Long> action) {
        return this.forLast((float l_t, long l_v) -> action.accept(l_t, l_v));
    }


    long putFirst(float key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(float, long)
     */
    @Deprecated
    default Long putFirst(Float key, Long value) {
        return this.putFirst(key != null ? key.floatValue() : (float) 0, value != null ? value.longValue() : (long) 0);
    }

    long putLast(float key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(float, long)
     */
    @Deprecated
    default Long putLast(Float key, Long value) {
        return this.putLast(key != null ? key.floatValue() : (float) 0, value != null ? value.longValue() : (long) 0);
    }

    FloatLongMap.Entry pollFirstEntry();

    boolean pollFirst(@NotNull FloatLongBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(FloatLongBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Float, ? super Long> action) {
        return this.pollFirst((float l_t, long l_v) -> action.accept(l_t, l_v));
    }

    FloatLongMap.Entry pollLastEntry();

    boolean pollLast(@NotNull FloatLongBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(FloatLongBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Float, ? super Long> action) {
        return this.pollLast((float l_t, long l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull FloatLongBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(FloatLongBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Float, ? super Long> action) {
        this.forEachReversed((float l_t, long l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    FloatOrderedSet keySet();

    @NotNull
    LongOrderedCollection values();

    @NotNull
    OrderedEntrySet<FloatLongMap.Entry> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Float, Long>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
