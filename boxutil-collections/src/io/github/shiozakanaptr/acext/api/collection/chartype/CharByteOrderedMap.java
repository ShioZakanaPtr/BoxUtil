package io.github.shiozakanaptr.acext.api.collection.chartype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.bytetype.ByteOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.chartype.CharByteBiConsumer;
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
public interface CharByteOrderedMap extends CharByteMap, OrderedMap<Character, Byte> {
    CharByteMap.Entry firstEntry();

    boolean forFirst(@NotNull CharByteBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(CharByteBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Character, ? super Byte> action) {
        return this.forFirst((char l_t, byte l_v) -> action.accept(l_t, l_v));
    }

    CharByteMap.Entry lastEntry();

    boolean forLast(@NotNull CharByteBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(CharByteBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Character, ? super Byte> action) {
        return this.forLast((char l_t, byte l_v) -> action.accept(l_t, l_v));
    }


    byte putFirst(char key, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(char, byte)
     */
    @Deprecated
    default Byte putFirst(Character key, Byte value) {
        return this.putFirst(key != null ? key.charValue() : (char) 0, value != null ? value.byteValue() : (byte) 0);
    }

    byte putLast(char key, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(char, byte)
     */
    @Deprecated
    default Byte putLast(Character key, Byte value) {
        return this.putLast(key != null ? key.charValue() : (char) 0, value != null ? value.byteValue() : (byte) 0);
    }

    CharByteMap.Entry pollFirstEntry();

    boolean pollFirst(@NotNull CharByteBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(CharByteBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Character, ? super Byte> action) {
        return this.pollFirst((char l_t, byte l_v) -> action.accept(l_t, l_v));
    }

    CharByteMap.Entry pollLastEntry();

    boolean pollLast(@NotNull CharByteBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(CharByteBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Character, ? super Byte> action) {
        return this.pollLast((char l_t, byte l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull CharByteBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(CharByteBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Character, ? super Byte> action) {
        this.forEachReversed((char l_t, byte l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    CharOrderedSet keySet();

    @NotNull
    ByteOrderedCollection values();

    @NotNull
    OrderedEntrySet<CharByteMap.Entry> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Character, Byte>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
