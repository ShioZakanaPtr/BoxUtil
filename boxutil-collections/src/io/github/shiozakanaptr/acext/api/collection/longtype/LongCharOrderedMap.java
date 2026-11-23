package io.github.shiozakanaptr.acext.api.collection.longtype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.chartype.CharOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.longtype.LongCharBiConsumer;
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
public interface LongCharOrderedMap extends LongCharMap, OrderedMap<Long, Character> {
    LongCharMap.Entry firstEntry();

    boolean forFirst(@NotNull LongCharBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(LongCharBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Long, ? super Character> action) {
        return this.forFirst((long l_t, char l_v) -> action.accept(l_t, l_v));
    }

    LongCharMap.Entry lastEntry();

    boolean forLast(@NotNull LongCharBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(LongCharBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Long, ? super Character> action) {
        return this.forLast((long l_t, char l_v) -> action.accept(l_t, l_v));
    }


    char putFirst(long key, char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(long, char)
     */
    @Deprecated
    default Character putFirst(Long key, Character value) {
        return this.putFirst(key != null ? key.longValue() : (long) 0, value != null ? value.charValue() : (char) 0);
    }

    char putLast(long key, char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(long, char)
     */
    @Deprecated
    default Character putLast(Long key, Character value) {
        return this.putLast(key != null ? key.longValue() : (long) 0, value != null ? value.charValue() : (char) 0);
    }

    LongCharMap.Entry pollFirstEntry();

    boolean pollFirst(@NotNull LongCharBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(LongCharBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Long, ? super Character> action) {
        return this.pollFirst((long l_t, char l_v) -> action.accept(l_t, l_v));
    }

    LongCharMap.Entry pollLastEntry();

    boolean pollLast(@NotNull LongCharBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(LongCharBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Long, ? super Character> action) {
        return this.pollLast((long l_t, char l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull LongCharBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(LongCharBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Long, ? super Character> action) {
        this.forEachReversed((long l_t, char l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    LongOrderedSet keySet();

    @NotNull
    CharOrderedCollection values();

    @NotNull
    OrderedEntrySet<LongCharMap.Entry> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Long, Character>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
