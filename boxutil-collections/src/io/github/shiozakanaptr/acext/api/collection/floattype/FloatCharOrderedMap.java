package io.github.shiozakanaptr.acext.api.collection.floattype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.chartype.CharOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.floattype.FloatCharBiConsumer;
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
public interface FloatCharOrderedMap extends FloatCharMap, OrderedMap<Float, Character> {
    FloatCharMap.Entry firstEntry();

    boolean forFirst(@NotNull FloatCharBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(FloatCharBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Float, ? super Character> action) {
        return this.forFirst((float l_t, char l_v) -> action.accept(l_t, l_v));
    }

    FloatCharMap.Entry lastEntry();

    boolean forLast(@NotNull FloatCharBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(FloatCharBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Float, ? super Character> action) {
        return this.forLast((float l_t, char l_v) -> action.accept(l_t, l_v));
    }


    char putFirst(float key, char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(float, char)
     */
    @Deprecated
    default Character putFirst(Float key, Character value) {
        return this.putFirst(key != null ? key.floatValue() : (float) 0, value != null ? value.charValue() : (char) 0);
    }

    char putLast(float key, char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(float, char)
     */
    @Deprecated
    default Character putLast(Float key, Character value) {
        return this.putLast(key != null ? key.floatValue() : (float) 0, value != null ? value.charValue() : (char) 0);
    }

    FloatCharMap.Entry pollFirstEntry();

    boolean pollFirst(@NotNull FloatCharBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(FloatCharBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Float, ? super Character> action) {
        return this.pollFirst((float l_t, char l_v) -> action.accept(l_t, l_v));
    }

    FloatCharMap.Entry pollLastEntry();

    boolean pollLast(@NotNull FloatCharBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(FloatCharBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Float, ? super Character> action) {
        return this.pollLast((float l_t, char l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull FloatCharBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(FloatCharBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Float, ? super Character> action) {
        this.forEachReversed((float l_t, char l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    FloatOrderedSet keySet();

    @NotNull
    CharOrderedCollection values();

    @NotNull
    OrderedEntrySet<FloatCharMap.Entry> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Float, Character>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
