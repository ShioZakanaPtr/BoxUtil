package io.github.shiozakanaptr.acext.api.collection.inttype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.chartype.CharOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.inttype.IntCharBiConsumer;
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
public interface IntCharOrderedMap extends IntCharMap, OrderedMap<Integer, Character> {
    IntCharMap.Entry firstEntry();

    boolean forFirst(@NotNull IntCharBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(IntCharBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Integer, ? super Character> action) {
        return this.forFirst((int l_t, char l_v) -> action.accept(l_t, l_v));
    }

    IntCharMap.Entry lastEntry();

    boolean forLast(@NotNull IntCharBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(IntCharBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Integer, ? super Character> action) {
        return this.forLast((int l_t, char l_v) -> action.accept(l_t, l_v));
    }


    char putFirst(int key, char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(int, char)
     */
    @Deprecated
    default Character putFirst(Integer key, Character value) {
        return this.putFirst(key != null ? key.intValue() : (int) 0, value != null ? value.charValue() : (char) 0);
    }

    char putLast(int key, char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(int, char)
     */
    @Deprecated
    default Character putLast(Integer key, Character value) {
        return this.putLast(key != null ? key.intValue() : (int) 0, value != null ? value.charValue() : (char) 0);
    }

    IntCharMap.Entry pollFirstEntry();

    boolean pollFirst(@NotNull IntCharBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(IntCharBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Integer, ? super Character> action) {
        return this.pollFirst((int l_t, char l_v) -> action.accept(l_t, l_v));
    }

    IntCharMap.Entry pollLastEntry();

    boolean pollLast(@NotNull IntCharBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(IntCharBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Integer, ? super Character> action) {
        return this.pollLast((int l_t, char l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull IntCharBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(IntCharBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Integer, ? super Character> action) {
        this.forEachReversed((int l_t, char l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    IntOrderedSet keySet();

    @NotNull
    CharOrderedCollection values();

    @NotNull
    OrderedEntrySet<IntCharMap.Entry> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Integer, Character>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
