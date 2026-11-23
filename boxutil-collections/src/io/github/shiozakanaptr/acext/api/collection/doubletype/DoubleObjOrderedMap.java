package io.github.shiozakanaptr.acext.api.collection.doubletype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.objecttype.ObjOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.doubletype.DoubleObjBiConsumer;
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
public interface DoubleObjOrderedMap<V> extends DoubleObjMap<V>, OrderedMap<Double, V> {
    DoubleObjMap.Entry<V> firstEntry();

    boolean forFirst(@NotNull DoubleObjBiConsumer<? super V> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(DoubleObjBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Double, ? super V> action) {
        return this.forFirst((double l_t, V l_v) -> action.accept(l_t, l_v));
    }

    DoubleObjMap.Entry<V> lastEntry();

    boolean forLast(@NotNull DoubleObjBiConsumer<? super V> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(DoubleObjBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Double, ? super V> action) {
        return this.forLast((double l_t, V l_v) -> action.accept(l_t, l_v));
    }


    V putFirst(double key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(double, V)
     */
    @Deprecated
    default V putFirst(Double key, V value) {
        return this.putFirst(key != null ? key.doubleValue() : (double) 0, value);
    }

    V putLast(double key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(double, V)
     */
    @Deprecated
    default V putLast(Double key, V value) {
        return this.putLast(key != null ? key.doubleValue() : (double) 0, value);
    }

    DoubleObjMap.Entry<V> pollFirstEntry();

    boolean pollFirst(@NotNull DoubleObjBiConsumer<? super V> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(DoubleObjBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Double, ? super V> action) {
        return this.pollFirst((double l_t, V l_v) -> action.accept(l_t, l_v));
    }

    DoubleObjMap.Entry<V> pollLastEntry();

    boolean pollLast(@NotNull DoubleObjBiConsumer<? super V> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(DoubleObjBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Double, ? super V> action) {
        return this.pollLast((double l_t, V l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull DoubleObjBiConsumer<? super V> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(DoubleObjBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Double, ? super V> action) {
        this.forEachReversed((double l_t, V l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    DoubleOrderedSet keySet();

    @NotNull
    ObjOrderedCollection<V> values();

    @NotNull
    OrderedEntrySet<DoubleObjMap.Entry<V>> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Double, V>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
