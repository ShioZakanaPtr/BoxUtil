package io.github.shiozakanaptr.acext.api.collection.chartype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.objecttype.ObjOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.chartype.CharObjBiConsumer;
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
public interface CharObjOrderedMap<V> extends CharObjMap<V>, OrderedMap<Character, V> {
    CharObjMap.Entry<V> firstEntry();

    boolean forFirst(@NotNull CharObjBiConsumer<? super V> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(CharObjBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Character, ? super V> action) {
        return this.forFirst((char l_t, V l_v) -> action.accept(l_t, l_v));
    }

    CharObjMap.Entry<V> lastEntry();

    boolean forLast(@NotNull CharObjBiConsumer<? super V> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(CharObjBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Character, ? super V> action) {
        return this.forLast((char l_t, V l_v) -> action.accept(l_t, l_v));
    }


    V putFirst(char key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(char, V)
     */
    @Deprecated
    default V putFirst(Character key, V value) {
        return this.putFirst(key != null ? key.charValue() : (char) 0, value);
    }

    V putLast(char key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(char, V)
     */
    @Deprecated
    default V putLast(Character key, V value) {
        return this.putLast(key != null ? key.charValue() : (char) 0, value);
    }

    CharObjMap.Entry<V> pollFirstEntry();

    boolean pollFirst(@NotNull CharObjBiConsumer<? super V> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(CharObjBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Character, ? super V> action) {
        return this.pollFirst((char l_t, V l_v) -> action.accept(l_t, l_v));
    }

    CharObjMap.Entry<V> pollLastEntry();

    boolean pollLast(@NotNull CharObjBiConsumer<? super V> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(CharObjBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Character, ? super V> action) {
        return this.pollLast((char l_t, V l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull CharObjBiConsumer<? super V> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(CharObjBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Character, ? super V> action) {
        this.forEachReversed((char l_t, V l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    CharOrderedSet keySet();

    @NotNull
    ObjOrderedCollection<V> values();

    @NotNull
    OrderedEntrySet<CharObjMap.Entry<V>> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Character, V>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
