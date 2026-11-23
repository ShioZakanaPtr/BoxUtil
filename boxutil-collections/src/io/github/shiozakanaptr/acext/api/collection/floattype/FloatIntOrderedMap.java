package io.github.shiozakanaptr.acext.api.collection.floattype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.inttype.IntOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.floattype.FloatIntBiConsumer;
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
public interface FloatIntOrderedMap extends FloatIntMap, OrderedMap<Float, Integer> {
    FloatIntMap.Entry firstEntry();

    boolean forFirst(@NotNull FloatIntBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(FloatIntBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Float, ? super Integer> action) {
        return this.forFirst((float l_t, int l_v) -> action.accept(l_t, l_v));
    }

    FloatIntMap.Entry lastEntry();

    boolean forLast(@NotNull FloatIntBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(FloatIntBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Float, ? super Integer> action) {
        return this.forLast((float l_t, int l_v) -> action.accept(l_t, l_v));
    }


    int putFirst(float key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(float, int)
     */
    @Deprecated
    default Integer putFirst(Float key, Integer value) {
        return this.putFirst(key != null ? key.floatValue() : (float) 0, value != null ? value.intValue() : (int) 0);
    }

    int putLast(float key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(float, int)
     */
    @Deprecated
    default Integer putLast(Float key, Integer value) {
        return this.putLast(key != null ? key.floatValue() : (float) 0, value != null ? value.intValue() : (int) 0);
    }

    FloatIntMap.Entry pollFirstEntry();

    boolean pollFirst(@NotNull FloatIntBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(FloatIntBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Float, ? super Integer> action) {
        return this.pollFirst((float l_t, int l_v) -> action.accept(l_t, l_v));
    }

    FloatIntMap.Entry pollLastEntry();

    boolean pollLast(@NotNull FloatIntBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(FloatIntBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Float, ? super Integer> action) {
        return this.pollLast((float l_t, int l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull FloatIntBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(FloatIntBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Float, ? super Integer> action) {
        this.forEachReversed((float l_t, int l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    FloatOrderedSet keySet();

    @NotNull
    IntOrderedCollection values();

    @NotNull
    OrderedEntrySet<FloatIntMap.Entry> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Float, Integer>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
