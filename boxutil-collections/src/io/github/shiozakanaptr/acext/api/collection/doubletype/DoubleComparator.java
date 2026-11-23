package io.github.shiozakanaptr.acext.api.collection.doubletype;

import io.github.shiozakanaptr.acext.util.function.doubletype.DoubleFunction;
import io.github.shiozakanaptr.acext.util.function.doubletype.DoubleToIntFunction;
import io.github.shiozakanaptr.acext.util.function.doubletype.DoubleToLongFunction;
import io.github.shiozakanaptr.acext.util.function.doubletype.DoubleToDoubleFunction;
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
public interface DoubleComparator extends Comparator<Double> {
    int compare(double o1, double o2);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compare(Double, Double)
     */
    @Deprecated
    default int compare(Double o1, Double o2) {
        return this.compare(o1 != null ? o1.doubleValue() : (double) 0, o2 != null ? o2.doubleValue() : (double) 0);
    }

    @NotNull
    default DoubleComparator reversed() {
        return new ReversedDoubleComparator(this);
    }

    @NotNull
    default DoubleComparator thenComparing(DoubleComparator other) {
        Objects.requireNonNull(other);
        return (DoubleComparator & Serializable) (double l_c1, double l_c2) -> {
            final int result;
            return (result = this.compare(l_c1, l_c2)) != 0 ? result : other.compare(l_c1, l_c2);
        };
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #thenComparing(DoubleComparator)
     */
    @Deprecated
    @NotNull
    default Comparator<Double> thenComparing(@NotNull Comparator<? super Double> other) {
        return this.thenComparing((double l_o1, double l_o2) -> other.compare(l_o1, l_o2));
    }

    @NotNull
    default <U> DoubleComparator thenComparing(@NotNull DoubleFunction<? extends U> keyExtractor, Comparator<? super U> keyComparator) {
        return this.thenComparing(DoubleComparator.comparing(keyExtractor, keyComparator));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #thenComparing(DoubleFunction, Comparator)
     */
    @Deprecated
    @NotNull
    default <U> Comparator<Double> thenComparing(@NotNull Function<? super Double, ? extends U> keyExtractor, Comparator<? super U> keyComparator) {
        return this.thenComparing((double l_value) -> keyExtractor.apply(l_value), keyComparator);
    }

    @NotNull
    default <U extends Comparable<? super U>> DoubleComparator thenComparing(@NotNull DoubleFunction<? extends U> keyExtractor) {
        return this.thenComparing(DoubleComparator.comparing(keyExtractor));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #thenComparing(DoubleFunction)
     */
    @Deprecated
    @NotNull
    default <U extends Comparable<? super U>> Comparator<Double> thenComparing(@NotNull Function<? super Double, ? extends U> keyExtractor) {
        return this.thenComparing((double l_value) -> keyExtractor.apply(l_value));
    }

    @NotNull
    default DoubleComparator thenComparingInt(@NotNull DoubleToIntFunction keyExtractor) {
        return this.thenComparing(DoubleComparator.comparingInt(keyExtractor));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #thenComparingInt(DoubleToIntFunction)
     */
    @Deprecated
    @NotNull
    default Comparator<Double> thenComparingInt(@NotNull ToIntFunction<? super Double> keyExtractor) {
        return this.thenComparingInt((double l_value) -> keyExtractor.applyAsInt(l_value));
    }

    @NotNull
    default DoubleComparator thenComparingLong(@NotNull DoubleToLongFunction keyExtractor) {
        return this.thenComparing(DoubleComparator.comparingLong(keyExtractor));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #thenComparingLong(DoubleToLongFunction)
     */
    @Deprecated
    @NotNull
    default Comparator<Double> thenComparingLong(@NotNull ToLongFunction<? super Double> keyExtractor) {
        return this.thenComparingLong((double l_value) -> keyExtractor.applyAsLong(l_value));
    }

    @NotNull
    default DoubleComparator thenComparingDouble(@NotNull DoubleToDoubleFunction keyExtractor) {
        return this.thenComparing(DoubleComparator.comparingDouble(keyExtractor));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #thenComparingDouble(DoubleToDoubleFunction)
     */
    @Deprecated
    @NotNull
    default Comparator<Double> thenComparingDouble(@NotNull ToDoubleFunction<? super Double> keyExtractor) {
        return this.thenComparingDouble((double l_value) -> keyExtractor.applyAsDouble(l_value));
    }

    @NotNull
    static <U> DoubleComparator comparing(@NotNull DoubleFunction<? extends U> keyExtractor, @NotNull Comparator<? super U> keyComparator) {
        Objects.requireNonNull(keyExtractor);
        Objects.requireNonNull(keyComparator);
        return (DoubleComparator & Serializable)
                (double l_c1, double l_c2) -> keyComparator.compare(keyExtractor.apply(l_c1), keyExtractor.apply(l_c2));
    }

    @NotNull
    static <U extends Comparable<? super U>> DoubleComparator comparing(@NotNull DoubleFunction<? extends U> keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (DoubleComparator & Serializable)
                (double l_c1, double l_c2) -> keyExtractor.apply(l_c1).compareTo(keyExtractor.apply(l_c2));
    }

    @NotNull
    static DoubleComparator comparingInt(@NotNull DoubleToIntFunction keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (DoubleComparator & Serializable)
                (double l_c1, double l_c2) -> Integer.compare(keyExtractor.apply(l_c1), keyExtractor.apply(l_c2));
    }

    @NotNull
    static DoubleComparator comparingLong(@NotNull DoubleToLongFunction keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (DoubleComparator & Serializable)
                (double l_c1, double l_c2) -> Long.compare(keyExtractor.apply(l_c1), keyExtractor.apply(l_c2));
    }

    @NotNull
    static DoubleComparator comparingDouble(@NotNull DoubleToDoubleFunction keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (DoubleComparator & Serializable)
                (double l_c1, double l_c2) -> Double.compare(keyExtractor.apply(l_c1), keyExtractor.apply(l_c2));
    }
}
