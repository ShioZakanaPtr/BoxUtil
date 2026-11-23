package io.github.shiozakanaptr.acext.api.collection.shorttype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.floattype.FloatOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.shorttype.ShortFloatBiConsumer;
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
public interface ShortFloatOrderedMap extends ShortFloatMap, OrderedMap<Short, Float> {
    ShortFloatMap.Entry firstEntry();

    boolean forFirst(@NotNull ShortFloatBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(ShortFloatBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Short, ? super Float> action) {
        return this.forFirst((short l_t, float l_v) -> action.accept(l_t, l_v));
    }

    ShortFloatMap.Entry lastEntry();

    boolean forLast(@NotNull ShortFloatBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(ShortFloatBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Short, ? super Float> action) {
        return this.forLast((short l_t, float l_v) -> action.accept(l_t, l_v));
    }


    float putFirst(short key, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(short, float)
     */
    @Deprecated
    default Float putFirst(Short key, Float value) {
        return this.putFirst(key != null ? key.shortValue() : (short) 0, value != null ? value.floatValue() : (float) 0);
    }

    float putLast(short key, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(short, float)
     */
    @Deprecated
    default Float putLast(Short key, Float value) {
        return this.putLast(key != null ? key.shortValue() : (short) 0, value != null ? value.floatValue() : (float) 0);
    }

    ShortFloatMap.Entry pollFirstEntry();

    boolean pollFirst(@NotNull ShortFloatBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(ShortFloatBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Short, ? super Float> action) {
        return this.pollFirst((short l_t, float l_v) -> action.accept(l_t, l_v));
    }

    ShortFloatMap.Entry pollLastEntry();

    boolean pollLast(@NotNull ShortFloatBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(ShortFloatBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Short, ? super Float> action) {
        return this.pollLast((short l_t, float l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull ShortFloatBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(ShortFloatBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Short, ? super Float> action) {
        this.forEachReversed((short l_t, float l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    ShortOrderedSet keySet();

    @NotNull
    FloatOrderedCollection values();

    @NotNull
    OrderedEntrySet<ShortFloatMap.Entry> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Short, Float>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
