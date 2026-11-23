package io.github.shiozakanaptr.acext.api.collection.inttype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.objecttype.ObjOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.inttype.IntObjBiConsumer;
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
public interface IntObjOrderedMap<V> extends IntObjMap<V>, OrderedMap<Integer, V> {
    IntObjMap.Entry<V> firstEntry();

    boolean forFirst(@NotNull IntObjBiConsumer<? super V> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(IntObjBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Integer, ? super V> action) {
        return this.forFirst((int l_t, V l_v) -> action.accept(l_t, l_v));
    }

    IntObjMap.Entry<V> lastEntry();

    boolean forLast(@NotNull IntObjBiConsumer<? super V> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(IntObjBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Integer, ? super V> action) {
        return this.forLast((int l_t, V l_v) -> action.accept(l_t, l_v));
    }


    V putFirst(int key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(int, V)
     */
    @Deprecated
    default V putFirst(Integer key, V value) {
        return this.putFirst(key != null ? key.intValue() : (int) 0, value);
    }

    V putLast(int key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(int, V)
     */
    @Deprecated
    default V putLast(Integer key, V value) {
        return this.putLast(key != null ? key.intValue() : (int) 0, value);
    }

    IntObjMap.Entry<V> pollFirstEntry();

    boolean pollFirst(@NotNull IntObjBiConsumer<? super V> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(IntObjBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Integer, ? super V> action) {
        return this.pollFirst((int l_t, V l_v) -> action.accept(l_t, l_v));
    }

    IntObjMap.Entry<V> pollLastEntry();

    boolean pollLast(@NotNull IntObjBiConsumer<? super V> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(IntObjBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Integer, ? super V> action) {
        return this.pollLast((int l_t, V l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull IntObjBiConsumer<? super V> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(IntObjBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Integer, ? super V> action) {
        this.forEachReversed((int l_t, V l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    IntOrderedSet keySet();

    @NotNull
    ObjOrderedCollection<V> values();

    @NotNull
    OrderedEntrySet<IntObjMap.Entry<V>> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Integer, V>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
