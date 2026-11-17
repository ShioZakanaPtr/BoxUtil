package io.github.shiozakanaptr.acext.api.collection.shorttype;

import io.github.shiozakanaptr.acext.api.collection.inttype.IntComparator;
import io.github.shiozakanaptr.acext.util.function.shorttype.ShortFunction;
import io.github.shiozakanaptr.acext.util.function.shorttype.ShortToIntFunction;
import io.github.shiozakanaptr.acext.util.function.shorttype.ShortToLongFunction;
import io.github.shiozakanaptr.acext.util.function.shorttype.ShortToDoubleFunction;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

@FunctionalInterface
public interface ShortComparator extends Comparator<Short> {
    int compare(short o1, short o2);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compare(Short, Short)
     */
    @Deprecated
    default int compare(Short o1, Short o2) {
        return this.compare(o1 != null ? o1.shortValue() : (short) 0, o2 != null ? o2.shortValue() : (short) 0);
    }

    @NotNull
    default ShortComparator reversed() {
        return new ReversedShortComparator(this);
    }

    @NotNull
    default ShortComparator thenComparing(ShortComparator other) {
        Objects.requireNonNull(other);
        return (ShortComparator & Serializable) (short l_c1, short l_c2) -> {
            final int result;
            return (result = this.compare(l_c1, l_c2)) != 0 ? result : other.compare(l_c1, l_c2);
        };
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #thenComparing(ShortComparator)
     */
    @Deprecated
    @NotNull
    default Comparator<Short> thenComparing(@NotNull Comparator<? super Short> other) {
        return this.thenComparing((short l_o1, short l_o2) -> other.compare(l_o1, l_o2));
    }

    @NotNull
    default <U> ShortComparator thenComparing(@NotNull ShortFunction<? extends U> keyExtractor, Comparator<? super U> keyComparator) {
        return this.thenComparing(ShortComparator.comparing(keyExtractor, keyComparator));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #thenComparing(ShortFunction, Comparator)
     */
    @Deprecated
    @NotNull
    default <U> Comparator<Short> thenComparing(@NotNull Function<? super Short, ? extends U> keyExtractor, Comparator<? super U> keyComparator) {
        return this.thenComparing((short l_value) -> keyExtractor.apply(l_value), keyComparator);
    }

    @NotNull
    default <U extends Comparable<? super U>> ShortComparator thenComparing(@NotNull ShortFunction<? extends U> keyExtractor) {
        return this.thenComparing(ShortComparator.comparing(keyExtractor));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #thenComparing(ShortFunction)
     */
    @Deprecated
    @NotNull
    default <U extends Comparable<? super U>> Comparator<Short> thenComparing(@NotNull Function<? super Short, ? extends U> keyExtractor) {
        return this.thenComparing((short l_value) -> keyExtractor.apply(l_value));
    }

    @NotNull
    default ShortComparator thenComparingInt(@NotNull ShortToIntFunction keyExtractor) {
        return this.thenComparing(ShortComparator.comparingInt(keyExtractor));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #thenComparingInt(ShortToIntFunction)
     */
    @Deprecated
    @NotNull
    default Comparator<Short> thenComparingInt(@NotNull ToIntFunction<? super Short> keyExtractor) {
        return this.thenComparingInt((short l_value) -> keyExtractor.applyAsInt(l_value));
    }

    @NotNull
    default ShortComparator thenComparingLong(@NotNull ShortToLongFunction keyExtractor) {
        return this.thenComparing(ShortComparator.comparingLong(keyExtractor));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #thenComparingLong(ShortToLongFunction)
     */
    @Deprecated
    @NotNull
    default Comparator<Short> thenComparingLong(@NotNull ToLongFunction<? super Short> keyExtractor) {
        return this.thenComparingLong((short l_value) -> keyExtractor.applyAsLong(l_value));
    }

    @NotNull
    default ShortComparator thenComparingDouble(@NotNull ShortToDoubleFunction keyExtractor) {
        return this.thenComparing(ShortComparator.comparingDouble(keyExtractor));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #thenComparingDouble(ShortToDoubleFunction)
     */
    @Deprecated
    @NotNull
    default Comparator<Short> thenComparingDouble(@NotNull ToDoubleFunction<? super Short> keyExtractor) {
        return this.thenComparingDouble((short l_value) -> keyExtractor.applyAsDouble(l_value));
    }

    @NotNull
    static <U> ShortComparator comparing(@NotNull ShortFunction<? extends U> keyExtractor, @NotNull Comparator<? super U> keyComparator) {
        Objects.requireNonNull(keyExtractor);
        Objects.requireNonNull(keyComparator);
        return (ShortComparator & Serializable)
                (short l_c1, short l_c2) -> keyComparator.compare(keyExtractor.apply(l_c1), keyExtractor.apply(l_c2));
    }

    @NotNull
    static <U extends Comparable<? super U>> ShortComparator comparing(@NotNull ShortFunction<? extends U> keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (ShortComparator & Serializable)
                (short l_c1, short l_c2) -> keyExtractor.apply(l_c1).compareTo(keyExtractor.apply(l_c2));
    }

    @NotNull
    static ShortComparator comparingInt(@NotNull ShortToIntFunction keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (ShortComparator & Serializable)
                (short l_c1, short l_c2) -> Integer.compare(keyExtractor.apply(l_c1), keyExtractor.apply(l_c2));
    }

    @NotNull
    static ShortComparator comparingLong(@NotNull ShortToLongFunction keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (ShortComparator & Serializable)
                (short l_c1, short l_c2) -> Long.compare(keyExtractor.apply(l_c1), keyExtractor.apply(l_c2));
    }

    @NotNull
    static ShortComparator comparingDouble(@NotNull ShortToDoubleFunction keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (ShortComparator & Serializable)
                (short l_c1, short l_c2) -> Double.compare(keyExtractor.apply(l_c1), keyExtractor.apply(l_c2));
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
    static IntComparator wrap(@NotNull ShortComparator source) {
        return new ShortWrapComparator(source);
    }
}
