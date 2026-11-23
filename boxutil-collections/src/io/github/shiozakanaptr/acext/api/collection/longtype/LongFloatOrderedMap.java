package io.github.shiozakanaptr.acext.api.collection.longtype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.floattype.FloatOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.longtype.LongFloatBiConsumer;
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
public interface LongFloatOrderedMap extends LongFloatMap, OrderedMap<Long, Float> {
    LongFloatMap.Entry firstEntry();

    boolean forFirst(@NotNull LongFloatBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(LongFloatBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Long, ? super Float> action) {
        return this.forFirst((long l_t, float l_v) -> action.accept(l_t, l_v));
    }

    LongFloatMap.Entry lastEntry();

    boolean forLast(@NotNull LongFloatBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(LongFloatBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Long, ? super Float> action) {
        return this.forLast((long l_t, float l_v) -> action.accept(l_t, l_v));
    }


    float putFirst(long key, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(long, float)
     */
    @Deprecated
    default Float putFirst(Long key, Float value) {
        return this.putFirst(key != null ? key.longValue() : (long) 0, value != null ? value.floatValue() : (float) 0);
    }

    float putLast(long key, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(long, float)
     */
    @Deprecated
    default Float putLast(Long key, Float value) {
        return this.putLast(key != null ? key.longValue() : (long) 0, value != null ? value.floatValue() : (float) 0);
    }

    LongFloatMap.Entry pollFirstEntry();

    boolean pollFirst(@NotNull LongFloatBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(LongFloatBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Long, ? super Float> action) {
        return this.pollFirst((long l_t, float l_v) -> action.accept(l_t, l_v));
    }

    LongFloatMap.Entry pollLastEntry();

    boolean pollLast(@NotNull LongFloatBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(LongFloatBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Long, ? super Float> action) {
        return this.pollLast((long l_t, float l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull LongFloatBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(LongFloatBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Long, ? super Float> action) {
        this.forEachReversed((long l_t, float l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    LongOrderedSet keySet();

    @NotNull
    FloatOrderedCollection values();

    @NotNull
    OrderedEntrySet<LongFloatMap.Entry> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Long, Float>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
