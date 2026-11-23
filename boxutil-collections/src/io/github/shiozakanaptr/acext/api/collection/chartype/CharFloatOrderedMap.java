package io.github.shiozakanaptr.acext.api.collection.chartype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;
import io.github.shiozakanaptr.acext.api.collection.floattype.FloatOrderedCollection;
import io.github.shiozakanaptr.acext.util.function.chartype.CharFloatBiConsumer;
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
public interface CharFloatOrderedMap extends CharFloatMap, OrderedMap<Character, Float> {
    CharFloatMap.Entry firstEntry();

    boolean forFirst(@NotNull CharFloatBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forFirst(CharFloatBiConsumer)
     */
    @Deprecated
    default boolean forFirst(@NotNull BiConsumer<? super Character, ? super Float> action) {
        return this.forFirst((char l_t, float l_v) -> action.accept(l_t, l_v));
    }

    CharFloatMap.Entry lastEntry();

    boolean forLast(@NotNull CharFloatBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forLast(CharFloatBiConsumer)
     */
    @Deprecated
    default boolean forLast(@NotNull BiConsumer<? super Character, ? super Float> action) {
        return this.forLast((char l_t, float l_v) -> action.accept(l_t, l_v));
    }


    float putFirst(char key, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putFirst(char, float)
     */
    @Deprecated
    default Float putFirst(Character key, Float value) {
        return this.putFirst(key != null ? key.charValue() : (char) 0, value != null ? value.floatValue() : (float) 0);
    }

    float putLast(char key, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putLast(char, float)
     */
    @Deprecated
    default Float putLast(Character key, Float value) {
        return this.putLast(key != null ? key.charValue() : (char) 0, value != null ? value.floatValue() : (float) 0);
    }

    CharFloatMap.Entry pollFirstEntry();

    boolean pollFirst(@NotNull CharFloatBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirst(CharFloatBiConsumer)
     */
    @Deprecated
    default boolean pollFirst(@NotNull BiConsumer<? super Character, ? super Float> action) {
        return this.pollFirst((char l_t, float l_v) -> action.accept(l_t, l_v));
    }

    CharFloatMap.Entry pollLastEntry();

    boolean pollLast(@NotNull CharFloatBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLast(CharFloatBiConsumer)
     */
    @Deprecated
    default boolean pollLast(@NotNull BiConsumer<? super Character, ? super Float> action) {
        return this.pollLast((char l_t, float l_v) -> action.accept(l_t, l_v));
    }

    void forEachReversed(@NotNull CharFloatBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachReversed(CharFloatBiConsumer)
     */
    @Deprecated
    default void forEachReversed(@NotNull BiConsumer<? super Character, ? super Float> action) {
        this.forEachReversed((char l_t, float l_v) -> action.accept(l_t, l_v));
    }

    @NotNull
    CharOrderedSet keySet();

    @NotNull
    FloatOrderedCollection values();

    @NotNull
    OrderedEntrySet<CharFloatMap.Entry> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<Character, Float>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
