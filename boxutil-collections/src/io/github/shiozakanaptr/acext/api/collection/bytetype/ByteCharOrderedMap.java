package io.github.shiozakanaptr.acext.api.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.chartype.CharOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.bytetype.ByteCharBiConsumer;
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
public interface ByteCharOrderedMap extends ByteCharMap, OrderedMap<Byte, Character> {
    ByteCharMap.Entry firstEntry();

    boolean forFirst(@NotNull ByteCharBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(ByteCharBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Byte, ? super Character> action) {
        return this.forFirst((byte l_t, char l_v) -> action.accept(l_t, l_v));
    }

    ByteCharMap.Entry lastEntry();

    boolean forLast(@NotNull ByteCharBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(ByteCharBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Byte, ? super Character> action) {
        return this.forLast((byte l_t, char l_v) -> action.accept(l_t, l_v));
    }


    char putFirst(byte key, char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(byte, char)
     */
    @Deprecated
    default Character putFirst(Byte key, Character value) {
        return this.putFirst(key != null ? key.byteValue() : (byte) 0, value != null ? value.charValue() : (char) 0);
    }

    char putLast(byte key, char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(byte, char)
     */
    @Deprecated
    default Character putLast(Byte key, Character value) {
        return this.putLast(key != null ? key.byteValue() : (byte) 0, value != null ? value.charValue() : (char) 0);
    }

    ByteCharMap.Entry pollFirstEntry();

    boolean pollFirst(@NotNull ByteCharBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(ByteCharBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Byte, ? super Character> action) {
        return this.pollFirst((byte l_t, char l_v) -> action.accept(l_t, l_v));
    }

    ByteCharMap.Entry pollLastEntry();

    boolean pollLast(@NotNull ByteCharBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(ByteCharBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Byte, ? super Character> action) {
        return this.pollLast((byte l_t, char l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull ByteCharBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(ByteCharBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Byte, ? super Character> action) {
        this.forEachReversed((byte l_t, char l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    ByteOrderedSet keySet();

    @NotNull
    CharOrderedCollection values();

    @NotNull
    OrderedEntrySet<ByteCharMap.Entry> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Byte, Character>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
