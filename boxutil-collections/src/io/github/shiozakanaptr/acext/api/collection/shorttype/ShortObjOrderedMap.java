package io.github.shiozakanaptr.acext.api.collection.shorttype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.objecttype.ObjOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.shorttype.ShortObjBiConsumer;
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
public interface ShortObjOrderedMap<V> extends ShortObjMap<V>, OrderedMap<Short, V> {
    ShortObjMap.Entry<V> firstEntry();

    boolean forFirst(@NotNull ShortObjBiConsumer<? super V> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(ShortObjBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Short, ? super V> action) {
        return this.forFirst((short l_t, V l_v) -> action.accept(l_t, l_v));
    }

    ShortObjMap.Entry<V> lastEntry();

    boolean forLast(@NotNull ShortObjBiConsumer<? super V> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(ShortObjBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Short, ? super V> action) {
        return this.forLast((short l_t, V l_v) -> action.accept(l_t, l_v));
    }


    V putFirst(short key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(short, V)
     */
    @Deprecated
    default V putFirst(Short key, V value) {
        return this.putFirst(key != null ? key.shortValue() : (short) 0, value);
    }

    V putLast(short key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(short, V)
     */
    @Deprecated
    default V putLast(Short key, V value) {
        return this.putLast(key != null ? key.shortValue() : (short) 0, value);
    }

    ShortObjMap.Entry<V> pollFirstEntry();

    boolean pollFirst(@NotNull ShortObjBiConsumer<? super V> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(ShortObjBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Short, ? super V> action) {
        return this.pollFirst((short l_t, V l_v) -> action.accept(l_t, l_v));
    }

    ShortObjMap.Entry<V> pollLastEntry();

    boolean pollLast(@NotNull ShortObjBiConsumer<? super V> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(ShortObjBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Short, ? super V> action) {
        return this.pollLast((short l_t, V l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull ShortObjBiConsumer<? super V> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(ShortObjBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Short, ? super V> action) {
        this.forEachReversed((short l_t, V l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    ShortOrderedSet keySet();

    @NotNull
    ObjOrderedCollection<V> values();

    @NotNull
    OrderedEntrySet<ShortObjMap.Entry<V>> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Short, V>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
