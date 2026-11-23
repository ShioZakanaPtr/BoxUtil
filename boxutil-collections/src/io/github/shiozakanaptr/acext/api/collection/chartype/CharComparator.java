package io.github.shiozakanaptr.acext.api.collection.chartype;

import io.github.shiozakanaptr.acext.api.collection.inttype.IntComparator;
import io.github.shiozakanaptr.acext.util.function.chartype.CharFunction;
import io.github.shiozakanaptr.acext.util.function.chartype.CharToIntFunction;
import io.github.shiozakanaptr.acext.util.function.chartype.CharToLongFunction;
import io.github.shiozakanaptr.acext.util.function.chartype.CharToDoubleFunction;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

/**
 * Type specialization interface.
 *
 * @see Comparator
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
@FunctionalInterface
public interface CharComparator extends Comparator<Character> {
    int compare(char o1, char o2);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compare(Character, Character)
     */
    @Deprecated
    default int compare(Character o1, Character o2) {
        return this.compare(o1 != null ? o1.charValue() : (char) 0, o2 != null ? o2.charValue() : (char) 0);
    }

    @NotNull
    default CharComparator reversed() {
        return new ReversedCharComparator(this);
    }

    @NotNull
    default CharComparator thenComparing(CharComparator other) {
        Objects.requireNonNull(other);
        return (CharComparator & Serializable) (char l_c1, char l_c2) -> {
            final int result;
            return (result = this.compare(l_c1, l_c2)) != 0 ? result : other.compare(l_c1, l_c2);
        };
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #thenComparing(CharComparator)
     */
    @Deprecated
    @NotNull
    default Comparator<Character> thenComparing(@NotNull Comparator<? super Character> other) {
        return this.thenComparing((char l_o1, char l_o2) -> other.compare(l_o1, l_o2));
    }

    @NotNull
    default <U> CharComparator thenComparing(@NotNull CharFunction<? extends U> keyExtractor, Comparator<? super U> keyComparator) {
        return this.thenComparing(CharComparator.comparing(keyExtractor, keyComparator));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #thenComparing(CharFunction, Comparator)
     */
    @Deprecated
    @NotNull
    default <U> Comparator<Character> thenComparing(@NotNull Function<? super Character, ? extends U> keyExtractor, Comparator<? super U> keyComparator) {
        return this.thenComparing((char l_value) -> keyExtractor.apply(l_value), keyComparator);
    }

    @NotNull
    default <U extends Comparable<? super U>> CharComparator thenComparing(@NotNull CharFunction<? extends U> keyExtractor) {
        return this.thenComparing(CharComparator.comparing(keyExtractor));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #thenComparing(CharFunction)
     */
    @Deprecated
    @NotNull
    default <U extends Comparable<? super U>> Comparator<Character> thenComparing(@NotNull Function<? super Character, ? extends U> keyExtractor) {
        return this.thenComparing((char l_value) -> keyExtractor.apply(l_value));
    }

    @NotNull
    default CharComparator thenComparingInt(@NotNull CharToIntFunction keyExtractor) {
        return this.thenComparing(CharComparator.comparingInt(keyExtractor));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #thenComparingInt(CharToIntFunction)
     */
    @Deprecated
    @NotNull
    default Comparator<Character> thenComparingInt(@NotNull ToIntFunction<? super Character> keyExtractor) {
        return this.thenComparingInt((char l_value) -> keyExtractor.applyAsInt(l_value));
    }

    @NotNull
    default CharComparator thenComparingLong(@NotNull CharToLongFunction keyExtractor) {
        return this.thenComparing(CharComparator.comparingLong(keyExtractor));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #thenComparingLong(CharToLongFunction)
     */
    @Deprecated
    @NotNull
    default Comparator<Character> thenComparingLong(@NotNull ToLongFunction<? super Character> keyExtractor) {
        return this.thenComparingLong((char l_value) -> keyExtractor.applyAsLong(l_value));
    }

    @NotNull
    default CharComparator thenComparingDouble(@NotNull CharToDoubleFunction keyExtractor) {
        return this.thenComparing(CharComparator.comparingDouble(keyExtractor));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #thenComparingDouble(CharToDoubleFunction)
     */
    @Deprecated
    @NotNull
    default Comparator<Character> thenComparingDouble(@NotNull ToDoubleFunction<? super Character> keyExtractor) {
        return this.thenComparingDouble((char l_value) -> keyExtractor.applyAsDouble(l_value));
    }

    @NotNull
    static <U> CharComparator comparing(@NotNull CharFunction<? extends U> keyExtractor, @NotNull Comparator<? super U> keyComparator) {
        Objects.requireNonNull(keyExtractor);
        Objects.requireNonNull(keyComparator);
        return (CharComparator & Serializable)
                (char l_c1, char l_c2) -> keyComparator.compare(keyExtractor.apply(l_c1), keyExtractor.apply(l_c2));
    }

    @NotNull
    static <U extends Comparable<? super U>> CharComparator comparing(@NotNull CharFunction<? extends U> keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (CharComparator & Serializable)
                (char l_c1, char l_c2) -> keyExtractor.apply(l_c1).compareTo(keyExtractor.apply(l_c2));
    }

    @NotNull
    static CharComparator comparingInt(@NotNull CharToIntFunction keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (CharComparator & Serializable)
                (char l_c1, char l_c2) -> Integer.compare(keyExtractor.apply(l_c1), keyExtractor.apply(l_c2));
    }

    @NotNull
    static CharComparator comparingLong(@NotNull CharToLongFunction keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (CharComparator & Serializable)
                (char l_c1, char l_c2) -> Long.compare(keyExtractor.apply(l_c1), keyExtractor.apply(l_c2));
    }

    @NotNull
    static CharComparator comparingDouble(@NotNull CharToDoubleFunction keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (CharComparator & Serializable)
                (char l_c1, char l_c2) -> Double.compare(keyExtractor.apply(l_c1), keyExtractor.apply(l_c2));
    }

    /**
     * Returns a comparator that wraps the given comparator, adapting it for
     * use with a comparator of a different but compatible element type.
     *
     * <p>The returned comparator delegates comparison to the given comparator.
     *
     * @param source the comparator to wrap; must not be {@code null}
     * @return a comparator adapted for the target comparator type
     * @throws NullPointerException if {@code source} is {@code null}
     */
    @NotNull
    static IntComparator wrap(@NotNull CharComparator source) {
        return new CharWrapComparator(source);
    }
}
