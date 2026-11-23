package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.floattype.FloatOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.objecttype.ObjFloatBiConsumer;
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
public interface ObjFloatOrderedMap<K> extends ObjFloatMap<K>, OrderedMap<K, Float> {
    ObjFloatMap.Entry<K> firstEntry();

    boolean forFirst(@NotNull ObjFloatBiConsumer<? super K> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(ObjFloatBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super K, ? super Float> action) {
        return this.forFirst((K l_t, float l_v) -> action.accept(l_t, l_v));
    }

    ObjFloatMap.Entry<K> lastEntry();

    boolean forLast(@NotNull ObjFloatBiConsumer<? super K> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(ObjFloatBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super K, ? super Float> action) {
        return this.forLast((K l_t, float l_v) -> action.accept(l_t, l_v));
    }


    float putFirst(K key, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(K, float)
     */
    @Deprecated
    default Float putFirst(K key, Float value) {
        return this.putFirst(key, value != null ? value.floatValue() : (float) 0);
    }

    float putLast(K key, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(K, float)
     */
    @Deprecated
    default Float putLast(K key, Float value) {
        return this.putLast(key, value != null ? value.floatValue() : (float) 0);
    }

    ObjFloatMap.Entry<K> pollFirstEntry();

    boolean pollFirst(@NotNull ObjFloatBiConsumer<? super K> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(ObjFloatBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super K, ? super Float> action) {
        return this.pollFirst((K l_t, float l_v) -> action.accept(l_t, l_v));
    }

    ObjFloatMap.Entry<K> pollLastEntry();

    boolean pollLast(@NotNull ObjFloatBiConsumer<? super K> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(ObjFloatBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super K, ? super Float> action) {
        return this.pollLast((K l_t, float l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull ObjFloatBiConsumer<? super K> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(ObjFloatBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super K, ? super Float> action) {
        this.forEachReversed((K l_t, float l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    ObjOrderedSet<K> keySet();

    @NotNull
    FloatOrderedCollection values();

    @NotNull
    OrderedEntrySet<ObjFloatMap.Entry<K>> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<K, Float>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
