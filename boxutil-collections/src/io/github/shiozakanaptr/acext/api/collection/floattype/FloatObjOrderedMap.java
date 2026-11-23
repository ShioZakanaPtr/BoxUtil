package io.github.shiozakanaptr.acext.api.collection.floattype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.objecttype.ObjOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.floattype.FloatObjBiConsumer;
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
public interface FloatObjOrderedMap<V> extends FloatObjMap<V>, OrderedMap<Float, V> {
    FloatObjMap.Entry<V> firstEntry();

    boolean forFirst(@NotNull FloatObjBiConsumer<? super V> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(FloatObjBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Float, ? super V> action) {
        return this.forFirst((float l_t, V l_v) -> action.accept(l_t, l_v));
    }

    FloatObjMap.Entry<V> lastEntry();

    boolean forLast(@NotNull FloatObjBiConsumer<? super V> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(FloatObjBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Float, ? super V> action) {
        return this.forLast((float l_t, V l_v) -> action.accept(l_t, l_v));
    }


    V putFirst(float key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(float, V)
     */
    @Deprecated
    default V putFirst(Float key, V value) {
        return this.putFirst(key != null ? key.floatValue() : (float) 0, value);
    }

    V putLast(float key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(float, V)
     */
    @Deprecated
    default V putLast(Float key, V value) {
        return this.putLast(key != null ? key.floatValue() : (float) 0, value);
    }

    FloatObjMap.Entry<V> pollFirstEntry();

    boolean pollFirst(@NotNull FloatObjBiConsumer<? super V> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(FloatObjBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Float, ? super V> action) {
        return this.pollFirst((float l_t, V l_v) -> action.accept(l_t, l_v));
    }

    FloatObjMap.Entry<V> pollLastEntry();

    boolean pollLast(@NotNull FloatObjBiConsumer<? super V> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(FloatObjBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Float, ? super V> action) {
        return this.pollLast((float l_t, V l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull FloatObjBiConsumer<? super V> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(FloatObjBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Float, ? super V> action) {
        this.forEachReversed((float l_t, V l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    FloatOrderedSet keySet();

    @NotNull
    ObjOrderedCollection<V> values();

    @NotNull
    OrderedEntrySet<FloatObjMap.Entry<V>> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Float, V>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
