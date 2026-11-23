package io.github.shiozakanaptr.acext.api.collection.chartype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.doubletype.DoubleOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.chartype.CharDoubleBiConsumer;
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
public interface CharDoubleOrderedMap extends CharDoubleMap, OrderedMap<Character, Double> {
    CharDoubleMap.Entry firstEntry();

    boolean forFirst(@NotNull CharDoubleBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(CharDoubleBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Character, ? super Double> action) {
        return this.forFirst((char l_t, double l_v) -> action.accept(l_t, l_v));
    }

    CharDoubleMap.Entry lastEntry();

    boolean forLast(@NotNull CharDoubleBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(CharDoubleBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Character, ? super Double> action) {
        return this.forLast((char l_t, double l_v) -> action.accept(l_t, l_v));
    }


    double putFirst(char key, double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(char, double)
     */
    @Deprecated
    default Double putFirst(Character key, Double value) {
        return this.putFirst(key != null ? key.charValue() : (char) 0, value != null ? value.doubleValue() : (double) 0);
    }

    double putLast(char key, double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(char, double)
     */
    @Deprecated
    default Double putLast(Character key, Double value) {
        return this.putLast(key != null ? key.charValue() : (char) 0, value != null ? value.doubleValue() : (double) 0);
    }

    CharDoubleMap.Entry pollFirstEntry();

    boolean pollFirst(@NotNull CharDoubleBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(CharDoubleBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Character, ? super Double> action) {
        return this.pollFirst((char l_t, double l_v) -> action.accept(l_t, l_v));
    }

    CharDoubleMap.Entry pollLastEntry();

    boolean pollLast(@NotNull CharDoubleBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(CharDoubleBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Character, ? super Double> action) {
        return this.pollLast((char l_t, double l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull CharDoubleBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(CharDoubleBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Character, ? super Double> action) {
        this.forEachReversed((char l_t, double l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    CharOrderedSet keySet();

    @NotNull
    DoubleOrderedCollection values();

    @NotNull
    OrderedEntrySet<CharDoubleMap.Entry> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Character, Double>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
