package io.github.shiozakanaptr.acext.api.collection.inttype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.floattype.FloatOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.inttype.IntFloatBiConsumer;
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
public interface IntFloatOrderedMap extends IntFloatMap, OrderedMap<Integer, Float> {
    IntFloatMap.Entry firstEntry();

    boolean forFirst(@NotNull IntFloatBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(IntFloatBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Integer, ? super Float> action) {
        return this.forFirst((int l_t, float l_v) -> action.accept(l_t, l_v));
    }

    IntFloatMap.Entry lastEntry();

    boolean forLast(@NotNull IntFloatBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(IntFloatBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Integer, ? super Float> action) {
        return this.forLast((int l_t, float l_v) -> action.accept(l_t, l_v));
    }


    float putFirst(int key, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(int, float)
     */
    @Deprecated
    default Float putFirst(Integer key, Float value) {
        return this.putFirst(key != null ? key.intValue() : (int) 0, value != null ? value.floatValue() : (float) 0);
    }

    float putLast(int key, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(int, float)
     */
    @Deprecated
    default Float putLast(Integer key, Float value) {
        return this.putLast(key != null ? key.intValue() : (int) 0, value != null ? value.floatValue() : (float) 0);
    }

    IntFloatMap.Entry pollFirstEntry();

    boolean pollFirst(@NotNull IntFloatBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(IntFloatBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Integer, ? super Float> action) {
        return this.pollFirst((int l_t, float l_v) -> action.accept(l_t, l_v));
    }

    IntFloatMap.Entry pollLastEntry();

    boolean pollLast(@NotNull IntFloatBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(IntFloatBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Integer, ? super Float> action) {
        return this.pollLast((int l_t, float l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull IntFloatBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(IntFloatBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Integer, ? super Float> action) {
        this.forEachReversed((int l_t, float l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    IntOrderedSet keySet();

    @NotNull
    FloatOrderedCollection values();

    @NotNull
    OrderedEntrySet<IntFloatMap.Entry> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Integer, Float>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
