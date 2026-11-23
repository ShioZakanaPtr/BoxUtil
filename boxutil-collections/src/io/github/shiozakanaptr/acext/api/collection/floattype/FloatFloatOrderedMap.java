package io.github.shiozakanaptr.acext.api.collection.floattype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.util.function.floattype.FloatFloatBiConsumer;
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
public interface FloatFloatOrderedMap extends FloatFloatMap, OrderedMap<Float, Float> {
    FloatFloatMap.Entry firstEntry();

    boolean forFirst(@NotNull FloatFloatBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(FloatFloatBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Float, ? super Float> action) {
        return this.forFirst((float l_t, float l_v) -> action.accept(l_t, l_v));
    }

    FloatFloatMap.Entry lastEntry();

    boolean forLast(@NotNull FloatFloatBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(FloatFloatBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Float, ? super Float> action) {
        return this.forLast((float l_t, float l_v) -> action.accept(l_t, l_v));
    }


    float putFirst(float key, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(float, float)
     */
    @Deprecated
    default Float putFirst(Float key, Float value) {
        return this.putFirst(key != null ? key.floatValue() : (float) 0, value != null ? value.floatValue() : (float) 0);
    }

    float putLast(float key, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(float, float)
     */
    @Deprecated
    default Float putLast(Float key, Float value) {
        return this.putLast(key != null ? key.floatValue() : (float) 0, value != null ? value.floatValue() : (float) 0);
    }

    FloatFloatMap.Entry pollFirstEntry();

    boolean pollFirst(@NotNull FloatFloatBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(FloatFloatBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Float, ? super Float> action) {
        return this.pollFirst((float l_t, float l_v) -> action.accept(l_t, l_v));
    }

    FloatFloatMap.Entry pollLastEntry();

    boolean pollLast(@NotNull FloatFloatBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(FloatFloatBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Float, ? super Float> action) {
        return this.pollLast((float l_t, float l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull FloatFloatBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(FloatFloatBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Float, ? super Float> action) {
        this.forEachReversed((float l_t, float l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    FloatOrderedSet keySet();

    @NotNull
    FloatOrderedCollection values();

    @NotNull
    OrderedEntrySet<FloatFloatMap.Entry> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Float, Float>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
