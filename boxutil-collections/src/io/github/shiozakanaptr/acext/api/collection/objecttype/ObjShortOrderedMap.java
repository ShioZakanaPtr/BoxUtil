package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.shorttype.ShortOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.objecttype.ObjShortBiConsumer;
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
public interface ObjShortOrderedMap<K> extends ObjShortMap<K>, OrderedMap<K, Short> {
    ObjShortMap.Entry<K> firstEntry();

    boolean forFirst(@NotNull ObjShortBiConsumer<? super K> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(ObjShortBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super K, ? super Short> action) {
        return this.forFirst((K l_t, short l_v) -> action.accept(l_t, l_v));
    }

    ObjShortMap.Entry<K> lastEntry();

    boolean forLast(@NotNull ObjShortBiConsumer<? super K> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(ObjShortBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super K, ? super Short> action) {
        return this.forLast((K l_t, short l_v) -> action.accept(l_t, l_v));
    }


    short putFirst(K key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(K, short)
     */
    @Deprecated
    default Short putFirst(K key, Short value) {
        return this.putFirst(key, value != null ? value.shortValue() : (short) 0);
    }

    short putLast(K key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(K, short)
     */
    @Deprecated
    default Short putLast(K key, Short value) {
        return this.putLast(key, value != null ? value.shortValue() : (short) 0);
    }

    ObjShortMap.Entry<K> pollFirstEntry();

    boolean pollFirst(@NotNull ObjShortBiConsumer<? super K> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(ObjShortBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super K, ? super Short> action) {
        return this.pollFirst((K l_t, short l_v) -> action.accept(l_t, l_v));
    }

    ObjShortMap.Entry<K> pollLastEntry();

    boolean pollLast(@NotNull ObjShortBiConsumer<? super K> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(ObjShortBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super K, ? super Short> action) {
        return this.pollLast((K l_t, short l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull ObjShortBiConsumer<? super K> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(ObjShortBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super K, ? super Short> action) {
        this.forEachReversed((K l_t, short l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    ObjOrderedSet<K> keySet();

    @NotNull
    ShortOrderedCollection values();

    @NotNull
    OrderedEntrySet<ObjShortMap.Entry<K>> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<K, Short>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
