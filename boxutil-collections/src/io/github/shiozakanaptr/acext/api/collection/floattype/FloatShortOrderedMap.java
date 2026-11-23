package io.github.shiozakanaptr.acext.api.collection.floattype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.shorttype.ShortOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.floattype.FloatShortBiConsumer;
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
public interface FloatShortOrderedMap extends FloatShortMap, OrderedMap<Float, Short> {
    FloatShortMap.Entry firstEntry();

    boolean forFirst(@NotNull FloatShortBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(FloatShortBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Float, ? super Short> action) {
        return this.forFirst((float l_t, short l_v) -> action.accept(l_t, l_v));
    }

    FloatShortMap.Entry lastEntry();

    boolean forLast(@NotNull FloatShortBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(FloatShortBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Float, ? super Short> action) {
        return this.forLast((float l_t, short l_v) -> action.accept(l_t, l_v));
    }


    short putFirst(float key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(float, short)
     */
    @Deprecated
    default Short putFirst(Float key, Short value) {
        return this.putFirst(key != null ? key.floatValue() : (float) 0, value != null ? value.shortValue() : (short) 0);
    }

    short putLast(float key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(float, short)
     */
    @Deprecated
    default Short putLast(Float key, Short value) {
        return this.putLast(key != null ? key.floatValue() : (float) 0, value != null ? value.shortValue() : (short) 0);
    }

    FloatShortMap.Entry pollFirstEntry();

    boolean pollFirst(@NotNull FloatShortBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(FloatShortBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Float, ? super Short> action) {
        return this.pollFirst((float l_t, short l_v) -> action.accept(l_t, l_v));
    }

    FloatShortMap.Entry pollLastEntry();

    boolean pollLast(@NotNull FloatShortBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(FloatShortBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Float, ? super Short> action) {
        return this.pollLast((float l_t, short l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull FloatShortBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(FloatShortBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Float, ? super Short> action) {
        this.forEachReversed((float l_t, short l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    FloatOrderedSet keySet();

    @NotNull
    ShortOrderedCollection values();

    @NotNull
    OrderedEntrySet<FloatShortMap.Entry> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Float, Short>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
