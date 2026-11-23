package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.doubletype.DoubleOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.objecttype.ObjDoubleBiConsumer;
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
public interface ObjDoubleOrderedMap<K> extends ObjDoubleMap<K>, OrderedMap<K, Double> {
    ObjDoubleMap.Entry<K> firstEntry();

    boolean forFirst(@NotNull ObjDoubleBiConsumer<? super K> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(ObjDoubleBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super K, ? super Double> action) {
        return this.forFirst((K l_t, double l_v) -> action.accept(l_t, l_v));
    }

    ObjDoubleMap.Entry<K> lastEntry();

    boolean forLast(@NotNull ObjDoubleBiConsumer<? super K> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(ObjDoubleBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super K, ? super Double> action) {
        return this.forLast((K l_t, double l_v) -> action.accept(l_t, l_v));
    }


    double putFirst(K key, double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(K, double)
     */
    @Deprecated
    default Double putFirst(K key, Double value) {
        return this.putFirst(key, value != null ? value.doubleValue() : (double) 0);
    }

    double putLast(K key, double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(K, double)
     */
    @Deprecated
    default Double putLast(K key, Double value) {
        return this.putLast(key, value != null ? value.doubleValue() : (double) 0);
    }

    ObjDoubleMap.Entry<K> pollFirstEntry();

    boolean pollFirst(@NotNull ObjDoubleBiConsumer<? super K> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(ObjDoubleBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super K, ? super Double> action) {
        return this.pollFirst((K l_t, double l_v) -> action.accept(l_t, l_v));
    }

    ObjDoubleMap.Entry<K> pollLastEntry();

    boolean pollLast(@NotNull ObjDoubleBiConsumer<? super K> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(ObjDoubleBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super K, ? super Double> action) {
        return this.pollLast((K l_t, double l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull ObjDoubleBiConsumer<? super K> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(ObjDoubleBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super K, ? super Double> action) {
        this.forEachReversed((K l_t, double l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    ObjOrderedSet<K> keySet();

    @NotNull
    DoubleOrderedCollection values();

    @NotNull
    OrderedEntrySet<ObjDoubleMap.Entry<K>> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<K, Double>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
