package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.longtype.LongOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.objecttype.ObjLongBiConsumer;
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
public interface ObjLongOrderedMap<K> extends ObjLongMap<K>, OrderedMap<K, Long> {
    ObjLongMap.Entry<K> firstEntry();

    boolean forFirst(@NotNull ObjLongBiConsumer<? super K> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(ObjLongBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super K, ? super Long> action) {
        return this.forFirst((K l_t, long l_v) -> action.accept(l_t, l_v));
    }

    ObjLongMap.Entry<K> lastEntry();

    boolean forLast(@NotNull ObjLongBiConsumer<? super K> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(ObjLongBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super K, ? super Long> action) {
        return this.forLast((K l_t, long l_v) -> action.accept(l_t, l_v));
    }


    long putFirst(K key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(K, long)
     */
    @Deprecated
    default Long putFirst(K key, Long value) {
        return this.putFirst(key, value != null ? value.longValue() : (long) 0);
    }

    long putLast(K key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(K, long)
     */
    @Deprecated
    default Long putLast(K key, Long value) {
        return this.putLast(key, value != null ? value.longValue() : (long) 0);
    }

    ObjLongMap.Entry<K> pollFirstEntry();

    boolean pollFirst(@NotNull ObjLongBiConsumer<? super K> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(ObjLongBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super K, ? super Long> action) {
        return this.pollFirst((K l_t, long l_v) -> action.accept(l_t, l_v));
    }

    ObjLongMap.Entry<K> pollLastEntry();

    boolean pollLast(@NotNull ObjLongBiConsumer<? super K> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(ObjLongBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super K, ? super Long> action) {
        return this.pollLast((K l_t, long l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull ObjLongBiConsumer<? super K> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(ObjLongBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super K, ? super Long> action) {
        this.forEachReversed((K l_t, long l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    ObjOrderedSet<K> keySet();

    @NotNull
    LongOrderedCollection values();

    @NotNull
    OrderedEntrySet<ObjLongMap.Entry<K>> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<K, Long>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
