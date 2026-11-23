package io.github.shiozakanaptr.acext.api.collection.chartype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.util.function.chartype.CharCharBiConsumer;
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
public interface CharCharOrderedMap extends CharCharMap, OrderedMap<Character, Character> {
    CharCharMap.Entry firstEntry();

    boolean forFirst(@NotNull CharCharBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(CharCharBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Character, ? super Character> action) {
        return this.forFirst((char l_t, char l_v) -> action.accept(l_t, l_v));
    }

    CharCharMap.Entry lastEntry();

    boolean forLast(@NotNull CharCharBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(CharCharBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Character, ? super Character> action) {
        return this.forLast((char l_t, char l_v) -> action.accept(l_t, l_v));
    }


    char putFirst(char key, char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(char, char)
     */
    @Deprecated
    default Character putFirst(Character key, Character value) {
        return this.putFirst(key != null ? key.charValue() : (char) 0, value != null ? value.charValue() : (char) 0);
    }

    char putLast(char key, char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(char, char)
     */
    @Deprecated
    default Character putLast(Character key, Character value) {
        return this.putLast(key != null ? key.charValue() : (char) 0, value != null ? value.charValue() : (char) 0);
    }

    CharCharMap.Entry pollFirstEntry();

    boolean pollFirst(@NotNull CharCharBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(CharCharBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Character, ? super Character> action) {
        return this.pollFirst((char l_t, char l_v) -> action.accept(l_t, l_v));
    }

    CharCharMap.Entry pollLastEntry();

    boolean pollLast(@NotNull CharCharBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(CharCharBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Character, ? super Character> action) {
        return this.pollLast((char l_t, char l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull CharCharBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(CharCharBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Character, ? super Character> action) {
        this.forEachReversed((char l_t, char l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    CharOrderedSet keySet();

    @NotNull
    CharOrderedCollection values();

    @NotNull
    OrderedEntrySet<CharCharMap.Entry> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Character, Character>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
