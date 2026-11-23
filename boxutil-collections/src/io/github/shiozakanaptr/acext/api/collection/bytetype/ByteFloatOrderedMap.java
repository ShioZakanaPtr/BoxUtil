package io.github.shiozakanaptr.acext.api.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.floattype.FloatOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.bytetype.ByteFloatBiConsumer;
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
public interface ByteFloatOrderedMap extends ByteFloatMap, OrderedMap<Byte, Float> {
    ByteFloatMap.Entry firstEntry();

    boolean forFirst(@NotNull ByteFloatBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(ByteFloatBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Byte, ? super Float> action) {
        return this.forFirst((byte l_t, float l_v) -> action.accept(l_t, l_v));
    }

    ByteFloatMap.Entry lastEntry();

    boolean forLast(@NotNull ByteFloatBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(ByteFloatBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Byte, ? super Float> action) {
        return this.forLast((byte l_t, float l_v) -> action.accept(l_t, l_v));
    }


    float putFirst(byte key, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(byte, float)
     */
    @Deprecated
    default Float putFirst(Byte key, Float value) {
        return this.putFirst(key != null ? key.byteValue() : (byte) 0, value != null ? value.floatValue() : (float) 0);
    }

    float putLast(byte key, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(byte, float)
     */
    @Deprecated
    default Float putLast(Byte key, Float value) {
        return this.putLast(key != null ? key.byteValue() : (byte) 0, value != null ? value.floatValue() : (float) 0);
    }

    ByteFloatMap.Entry pollFirstEntry();

    boolean pollFirst(@NotNull ByteFloatBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(ByteFloatBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Byte, ? super Float> action) {
        return this.pollFirst((byte l_t, float l_v) -> action.accept(l_t, l_v));
    }

    ByteFloatMap.Entry pollLastEntry();

    boolean pollLast(@NotNull ByteFloatBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(ByteFloatBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Byte, ? super Float> action) {
        return this.pollLast((byte l_t, float l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull ByteFloatBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(ByteFloatBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Byte, ? super Float> action) {
        this.forEachReversed((byte l_t, float l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    ByteOrderedSet keySet();

    @NotNull
    FloatOrderedCollection values();

    @NotNull
    OrderedEntrySet<ByteFloatMap.Entry> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Byte, Float>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
