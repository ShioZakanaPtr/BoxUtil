package io.github.shiozakanaptr.acext.api.collection.doubletype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.chartype.CharOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.doubletype.DoubleCharBiConsumer;
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
public interface DoubleCharOrderedMap extends DoubleCharMap, OrderedMap<Double, Character> {
    DoubleCharMap.Entry firstEntry();

    boolean forFirst(@NotNull DoubleCharBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(DoubleCharBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Double, ? super Character> action) {
        return this.forFirst((double l_t, char l_v) -> action.accept(l_t, l_v));
    }

    DoubleCharMap.Entry lastEntry();

    boolean forLast(@NotNull DoubleCharBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(DoubleCharBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Double, ? super Character> action) {
        return this.forLast((double l_t, char l_v) -> action.accept(l_t, l_v));
    }


    char putFirst(double key, char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(double, char)
     */
    @Deprecated
    default Character putFirst(Double key, Character value) {
        return this.putFirst(key != null ? key.doubleValue() : (double) 0, value != null ? value.charValue() : (char) 0);
    }

    char putLast(double key, char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(double, char)
     */
    @Deprecated
    default Character putLast(Double key, Character value) {
        return this.putLast(key != null ? key.doubleValue() : (double) 0, value != null ? value.charValue() : (char) 0);
    }

    DoubleCharMap.Entry pollFirstEntry();

    boolean pollFirst(@NotNull DoubleCharBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(DoubleCharBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Double, ? super Character> action) {
        return this.pollFirst((double l_t, char l_v) -> action.accept(l_t, l_v));
    }

    DoubleCharMap.Entry pollLastEntry();

    boolean pollLast(@NotNull DoubleCharBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(DoubleCharBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Double, ? super Character> action) {
        return this.pollLast((double l_t, char l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull DoubleCharBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(DoubleCharBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Double, ? super Character> action) {
        this.forEachReversed((double l_t, char l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    DoubleOrderedSet keySet();

    @NotNull
    CharOrderedCollection values();

    @NotNull
    OrderedEntrySet<DoubleCharMap.Entry> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Double, Character>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
