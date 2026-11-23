package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.chartype.CharOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.objecttype.ObjCharBiConsumer;
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
public interface ObjCharOrderedMap<K> extends ObjCharMap<K>, OrderedMap<K, Character> {
    ObjCharMap.Entry<K> firstEntry();

    boolean forFirst(@NotNull ObjCharBiConsumer<? super K> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(ObjCharBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super K, ? super Character> action) {
        return this.forFirst((K l_t, char l_v) -> action.accept(l_t, l_v));
    }

    ObjCharMap.Entry<K> lastEntry();

    boolean forLast(@NotNull ObjCharBiConsumer<? super K> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(ObjCharBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super K, ? super Character> action) {
        return this.forLast((K l_t, char l_v) -> action.accept(l_t, l_v));
    }


    char putFirst(K key, char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(K, char)
     */
    @Deprecated
    default Character putFirst(K key, Character value) {
        return this.putFirst(key, value != null ? value.charValue() : (char) 0);
    }

    char putLast(K key, char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(K, char)
     */
    @Deprecated
    default Character putLast(K key, Character value) {
        return this.putLast(key, value != null ? value.charValue() : (char) 0);
    }

    ObjCharMap.Entry<K> pollFirstEntry();

    boolean pollFirst(@NotNull ObjCharBiConsumer<? super K> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(ObjCharBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super K, ? super Character> action) {
        return this.pollFirst((K l_t, char l_v) -> action.accept(l_t, l_v));
    }

    ObjCharMap.Entry<K> pollLastEntry();

    boolean pollLast(@NotNull ObjCharBiConsumer<? super K> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(ObjCharBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super K, ? super Character> action) {
        return this.pollLast((K l_t, char l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull ObjCharBiConsumer<? super K> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(ObjCharBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super K, ? super Character> action) {
        this.forEachReversed((K l_t, char l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    ObjOrderedSet<K> keySet();

    @NotNull
    CharOrderedCollection values();

    @NotNull
    OrderedEntrySet<ObjCharMap.Entry<K>> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<K, Character>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
