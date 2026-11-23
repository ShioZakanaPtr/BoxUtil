package io.github.shiozakanaptr.acext.api.collection.chartype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.shorttype.ShortOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.chartype.CharShortBiConsumer;
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
public interface CharShortOrderedMap extends CharShortMap, OrderedMap<Character, Short> {
    CharShortMap.Entry firstEntry();

    boolean forFirst(@NotNull CharShortBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(CharShortBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Character, ? super Short> action) {
        return this.forFirst((char l_t, short l_v) -> action.accept(l_t, l_v));
    }

    CharShortMap.Entry lastEntry();

    boolean forLast(@NotNull CharShortBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(CharShortBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Character, ? super Short> action) {
        return this.forLast((char l_t, short l_v) -> action.accept(l_t, l_v));
    }


    short putFirst(char key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(char, short)
     */
    @Deprecated
    default Short putFirst(Character key, Short value) {
        return this.putFirst(key != null ? key.charValue() : (char) 0, value != null ? value.shortValue() : (short) 0);
    }

    short putLast(char key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(char, short)
     */
    @Deprecated
    default Short putLast(Character key, Short value) {
        return this.putLast(key != null ? key.charValue() : (char) 0, value != null ? value.shortValue() : (short) 0);
    }

    CharShortMap.Entry pollFirstEntry();

    boolean pollFirst(@NotNull CharShortBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(CharShortBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Character, ? super Short> action) {
        return this.pollFirst((char l_t, short l_v) -> action.accept(l_t, l_v));
    }

    CharShortMap.Entry pollLastEntry();

    boolean pollLast(@NotNull CharShortBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(CharShortBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Character, ? super Short> action) {
        return this.pollLast((char l_t, short l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull CharShortBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(CharShortBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Character, ? super Short> action) {
        this.forEachReversed((char l_t, short l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    CharOrderedSet keySet();

    @NotNull
    ShortOrderedCollection values();

    @NotNull
    OrderedEntrySet<CharShortMap.Entry> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Character, Short>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
