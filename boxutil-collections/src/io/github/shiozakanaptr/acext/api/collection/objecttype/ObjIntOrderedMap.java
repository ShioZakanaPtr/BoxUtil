package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.inttype.IntOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.objecttype.ObjIntBiConsumer;
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
public interface ObjIntOrderedMap<K> extends ObjIntMap<K>, OrderedMap<K, Integer> {
    ObjIntMap.Entry<K> firstEntry();

    boolean forFirst(@NotNull ObjIntBiConsumer<? super K> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(ObjIntBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super K, ? super Integer> action) {
        return this.forFirst((K l_t, int l_v) -> action.accept(l_t, l_v));
    }

    ObjIntMap.Entry<K> lastEntry();

    boolean forLast(@NotNull ObjIntBiConsumer<? super K> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(ObjIntBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super K, ? super Integer> action) {
        return this.forLast((K l_t, int l_v) -> action.accept(l_t, l_v));
    }


    int putFirst(K key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(K, int)
     */
    @Deprecated
    default Integer putFirst(K key, Integer value) {
        return this.putFirst(key, value != null ? value.intValue() : (int) 0);
    }

    int putLast(K key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(K, int)
     */
    @Deprecated
    default Integer putLast(K key, Integer value) {
        return this.putLast(key, value != null ? value.intValue() : (int) 0);
    }

    ObjIntMap.Entry<K> pollFirstEntry();

    boolean pollFirst(@NotNull ObjIntBiConsumer<? super K> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(ObjIntBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super K, ? super Integer> action) {
        return this.pollFirst((K l_t, int l_v) -> action.accept(l_t, l_v));
    }

    ObjIntMap.Entry<K> pollLastEntry();

    boolean pollLast(@NotNull ObjIntBiConsumer<? super K> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(ObjIntBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super K, ? super Integer> action) {
        return this.pollLast((K l_t, int l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull ObjIntBiConsumer<? super K> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(ObjIntBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super K, ? super Integer> action) {
        this.forEachReversed((K l_t, int l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    ObjOrderedSet<K> keySet();

    @NotNull
    IntOrderedCollection values();

    @NotNull
    OrderedEntrySet<ObjIntMap.Entry<K>> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<K, Integer>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
