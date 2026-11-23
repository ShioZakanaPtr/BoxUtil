package io.github.shiozakanaptr.acext.api.collection.shorttype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.chartype.CharOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.shorttype.ShortCharBiConsumer;
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
public interface ShortCharOrderedMap extends ShortCharMap, OrderedMap<Short, Character> {
    ShortCharMap.Entry firstEntry();

    boolean forFirst(@NotNull ShortCharBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(ShortCharBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Short, ? super Character> action) {
        return this.forFirst((short l_t, char l_v) -> action.accept(l_t, l_v));
    }

    ShortCharMap.Entry lastEntry();

    boolean forLast(@NotNull ShortCharBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(ShortCharBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Short, ? super Character> action) {
        return this.forLast((short l_t, char l_v) -> action.accept(l_t, l_v));
    }


    char putFirst(short key, char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(short, char)
     */
    @Deprecated
    default Character putFirst(Short key, Character value) {
        return this.putFirst(key != null ? key.shortValue() : (short) 0, value != null ? value.charValue() : (char) 0);
    }

    char putLast(short key, char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(short, char)
     */
    @Deprecated
    default Character putLast(Short key, Character value) {
        return this.putLast(key != null ? key.shortValue() : (short) 0, value != null ? value.charValue() : (char) 0);
    }

    ShortCharMap.Entry pollFirstEntry();

    boolean pollFirst(@NotNull ShortCharBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(ShortCharBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Short, ? super Character> action) {
        return this.pollFirst((short l_t, char l_v) -> action.accept(l_t, l_v));
    }

    ShortCharMap.Entry pollLastEntry();

    boolean pollLast(@NotNull ShortCharBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(ShortCharBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Short, ? super Character> action) {
        return this.pollLast((short l_t, char l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull ShortCharBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(ShortCharBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Short, ? super Character> action) {
        this.forEachReversed((short l_t, char l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    ShortOrderedSet keySet();

    @NotNull
    CharOrderedCollection values();

    @NotNull
    OrderedEntrySet<ShortCharMap.Entry> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Short, Character>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
