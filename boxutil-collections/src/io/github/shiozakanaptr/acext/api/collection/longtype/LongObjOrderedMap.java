package io.github.shiozakanaptr.acext.api.collection.longtype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.objecttype.ObjOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.longtype.LongObjBiConsumer;
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
public interface LongObjOrderedMap<V> extends LongObjMap<V>, OrderedMap<Long, V> {
    LongObjMap.Entry<V> firstEntry();

    boolean forFirst(@NotNull LongObjBiConsumer<? super V> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(LongObjBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Long, ? super V> action) {
        return this.forFirst((long l_t, V l_v) -> action.accept(l_t, l_v));
    }

    LongObjMap.Entry<V> lastEntry();

    boolean forLast(@NotNull LongObjBiConsumer<? super V> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(LongObjBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Long, ? super V> action) {
        return this.forLast((long l_t, V l_v) -> action.accept(l_t, l_v));
    }


    V putFirst(long key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(long, V)
     */
    @Deprecated
    default V putFirst(Long key, V value) {
        return this.putFirst(key != null ? key.longValue() : (long) 0, value);
    }

    V putLast(long key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(long, V)
     */
    @Deprecated
    default V putLast(Long key, V value) {
        return this.putLast(key != null ? key.longValue() : (long) 0, value);
    }

    LongObjMap.Entry<V> pollFirstEntry();

    boolean pollFirst(@NotNull LongObjBiConsumer<? super V> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(LongObjBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Long, ? super V> action) {
        return this.pollFirst((long l_t, V l_v) -> action.accept(l_t, l_v));
    }

    LongObjMap.Entry<V> pollLastEntry();

    boolean pollLast(@NotNull LongObjBiConsumer<? super V> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(LongObjBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Long, ? super V> action) {
        return this.pollLast((long l_t, V l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull LongObjBiConsumer<? super V> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(LongObjBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Long, ? super V> action) {
        this.forEachReversed((long l_t, V l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    LongOrderedSet keySet();

    @NotNull
    ObjOrderedCollection<V> values();

    @NotNull
    OrderedEntrySet<LongObjMap.Entry<V>> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Long, V>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
