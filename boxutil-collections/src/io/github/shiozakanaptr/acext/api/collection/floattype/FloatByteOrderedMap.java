package io.github.shiozakanaptr.acext.api.collection.floattype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.bytetype.ByteOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.floattype.FloatByteBiConsumer;
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
public interface FloatByteOrderedMap extends FloatByteMap, OrderedMap<Float, Byte> {
    FloatByteMap.Entry firstEntry();

    boolean forFirst(@NotNull FloatByteBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(FloatByteBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Float, ? super Byte> action) {
        return this.forFirst((float l_t, byte l_v) -> action.accept(l_t, l_v));
    }

    FloatByteMap.Entry lastEntry();

    boolean forLast(@NotNull FloatByteBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(FloatByteBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Float, ? super Byte> action) {
        return this.forLast((float l_t, byte l_v) -> action.accept(l_t, l_v));
    }


    byte putFirst(float key, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(float, byte)
     */
    @Deprecated
    default Byte putFirst(Float key, Byte value) {
        return this.putFirst(key != null ? key.floatValue() : (float) 0, value != null ? value.byteValue() : (byte) 0);
    }

    byte putLast(float key, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(float, byte)
     */
    @Deprecated
    default Byte putLast(Float key, Byte value) {
        return this.putLast(key != null ? key.floatValue() : (float) 0, value != null ? value.byteValue() : (byte) 0);
    }

    FloatByteMap.Entry pollFirstEntry();

    boolean pollFirst(@NotNull FloatByteBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(FloatByteBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Float, ? super Byte> action) {
        return this.pollFirst((float l_t, byte l_v) -> action.accept(l_t, l_v));
    }

    FloatByteMap.Entry pollLastEntry();

    boolean pollLast(@NotNull FloatByteBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(FloatByteBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Float, ? super Byte> action) {
        return this.pollLast((float l_t, byte l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull FloatByteBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(FloatByteBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Float, ? super Byte> action) {
        this.forEachReversed((float l_t, byte l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    FloatOrderedSet keySet();

    @NotNull
    ByteOrderedCollection values();

    @NotNull
    OrderedEntrySet<FloatByteMap.Entry> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Float, Byte>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
