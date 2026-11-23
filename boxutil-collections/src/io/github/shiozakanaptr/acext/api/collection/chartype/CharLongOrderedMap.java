package io.github.shiozakanaptr.acext.api.collection.chartype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.longtype.LongOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.chartype.CharLongBiConsumer;
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
public interface CharLongOrderedMap extends CharLongMap, OrderedMap<Character, Long> {
    CharLongMap.Entry firstEntry();

    boolean forFirst(@NotNull CharLongBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(CharLongBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Character, ? super Long> action) {
        return this.forFirst((char l_t, long l_v) -> action.accept(l_t, l_v));
    }

    CharLongMap.Entry lastEntry();

    boolean forLast(@NotNull CharLongBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(CharLongBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Character, ? super Long> action) {
        return this.forLast((char l_t, long l_v) -> action.accept(l_t, l_v));
    }


    long putFirst(char key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(char, long)
     */
    @Deprecated
    default Long putFirst(Character key, Long value) {
        return this.putFirst(key != null ? key.charValue() : (char) 0, value != null ? value.longValue() : (long) 0);
    }

    long putLast(char key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(char, long)
     */
    @Deprecated
    default Long putLast(Character key, Long value) {
        return this.putLast(key != null ? key.charValue() : (char) 0, value != null ? value.longValue() : (long) 0);
    }

    CharLongMap.Entry pollFirstEntry();

    boolean pollFirst(@NotNull CharLongBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(CharLongBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Character, ? super Long> action) {
        return this.pollFirst((char l_t, long l_v) -> action.accept(l_t, l_v));
    }

    CharLongMap.Entry pollLastEntry();

    boolean pollLast(@NotNull CharLongBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(CharLongBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Character, ? super Long> action) {
        return this.pollLast((char l_t, long l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull CharLongBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(CharLongBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Character, ? super Long> action) {
        this.forEachReversed((char l_t, long l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    CharOrderedSet keySet();

    @NotNull
    LongOrderedCollection values();

    @NotNull
    OrderedEntrySet<CharLongMap.Entry> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Character, Long>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
