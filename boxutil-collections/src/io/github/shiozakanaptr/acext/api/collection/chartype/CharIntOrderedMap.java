package io.github.shiozakanaptr.acext.api.collection.chartype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.inttype.IntOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.chartype.CharIntBiConsumer;
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
public interface CharIntOrderedMap extends CharIntMap, OrderedMap<Character, Integer> {
    CharIntMap.Entry firstEntry();

    boolean forFirst(@NotNull CharIntBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(CharIntBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Character, ? super Integer> action) {
        return this.forFirst((char l_t, int l_v) -> action.accept(l_t, l_v));
    }

    CharIntMap.Entry lastEntry();

    boolean forLast(@NotNull CharIntBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(CharIntBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Character, ? super Integer> action) {
        return this.forLast((char l_t, int l_v) -> action.accept(l_t, l_v));
    }


    int putFirst(char key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(char, int)
     */
    @Deprecated
    default Integer putFirst(Character key, Integer value) {
        return this.putFirst(key != null ? key.charValue() : (char) 0, value != null ? value.intValue() : (int) 0);
    }

    int putLast(char key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(char, int)
     */
    @Deprecated
    default Integer putLast(Character key, Integer value) {
        return this.putLast(key != null ? key.charValue() : (char) 0, value != null ? value.intValue() : (int) 0);
    }

    CharIntMap.Entry pollFirstEntry();

    boolean pollFirst(@NotNull CharIntBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(CharIntBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Character, ? super Integer> action) {
        return this.pollFirst((char l_t, int l_v) -> action.accept(l_t, l_v));
    }

    CharIntMap.Entry pollLastEntry();

    boolean pollLast(@NotNull CharIntBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(CharIntBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Character, ? super Integer> action) {
        return this.pollLast((char l_t, int l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull CharIntBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(CharIntBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Character, ? super Integer> action) {
        this.forEachReversed((char l_t, int l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    CharOrderedSet keySet();

    @NotNull
    IntOrderedCollection values();

    @NotNull
    OrderedEntrySet<CharIntMap.Entry> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Character, Integer>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
