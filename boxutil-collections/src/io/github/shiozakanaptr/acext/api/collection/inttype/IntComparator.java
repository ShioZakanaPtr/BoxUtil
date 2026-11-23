package io.github.shiozakanaptr.acext.api.collection.inttype;

import io.github.shiozakanaptr.acext.util.function.inttype.IntFunction;
import io.github.shiozakanaptr.acext.util.function.inttype.IntToIntFunction;
import io.github.shiozakanaptr.acext.util.function.inttype.IntToLongFunction;
import io.github.shiozakanaptr.acext.util.function.inttype.IntToDoubleFunction;
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
public interface IntComparator extends Comparator<Integer> {
    int compare(int o1, int o2);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compare(Integer, Integer)
     */
    @Deprecated
    default int compare(Integer o1, Integer o2) {
        return this.compare(o1 != null ? o1.intValue() : (int) 0, o2 != null ? o2.intValue() : (int) 0);
    }

    @NotNull
    default IntComparator reversed() {
        return new ReversedIntComparator(this);
    }

    @NotNull
    default IntComparator thenComparing(IntComparator other) {
        Objects.requireNonNull(other);
        return (IntComparator & Serializable) (int l_c1, int l_c2) -> {
            final int result;
            return (result = this.compare(l_c1, l_c2)) != 0 ? result : other.compare(l_c1, l_c2);
        };
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #thenComparing(IntComparator)
     */
    @Deprecated
    @NotNull
    default Comparator<Integer> thenComparing(@NotNull Comparator<? super Integer> other) {
        return this.thenComparing((int l_o1, int l_o2) -> other.compare(l_o1, l_o2));
    }

    @NotNull
    default <U> IntComparator thenComparing(@NotNull IntFunction<? extends U> keyExtractor, Comparator<? super U> keyComparator) {
        return this.thenComparing(IntComparator.comparing(keyExtractor, keyComparator));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #thenComparing(IntFunction, Comparator)
     */
    @Deprecated
    @NotNull
    default <U> Comparator<Integer> thenComparing(@NotNull Function<? super Integer, ? extends U> keyExtractor, Comparator<? super U> keyComparator) {
        return this.thenComparing((int l_value) -> keyExtractor.apply(l_value), keyComparator);
    }

    @NotNull
    default <U extends Comparable<? super U>> IntComparator thenComparing(@NotNull IntFunction<? extends U> keyExtractor) {
        return this.thenComparing(IntComparator.comparing(keyExtractor));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #thenComparing(IntFunction)
     */
    @Deprecated
    @NotNull
    default <U extends Comparable<? super U>> Comparator<Integer> thenComparing(@NotNull Function<? super Integer, ? extends U> keyExtractor) {
        return this.thenComparing((int l_value) -> keyExtractor.apply(l_value));
    }

    @NotNull
    default IntComparator thenComparingInt(@NotNull IntToIntFunction keyExtractor) {
        return this.thenComparing(IntComparator.comparingInt(keyExtractor));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #thenComparingInt(IntToIntFunction)
     */
    @Deprecated
    @NotNull
    default Comparator<Integer> thenComparingInt(@NotNull ToIntFunction<? super Integer> keyExtractor) {
        return this.thenComparingInt((int l_value) -> keyExtractor.applyAsInt(l_value));
    }

    @NotNull
    default IntComparator thenComparingLong(@NotNull IntToLongFunction keyExtractor) {
        return this.thenComparing(IntComparator.comparingLong(keyExtractor));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #thenComparingLong(IntToLongFunction)
     */
    @Deprecated
    @NotNull
    default Comparator<Integer> thenComparingLong(@NotNull ToLongFunction<? super Integer> keyExtractor) {
        return this.thenComparingLong((int l_value) -> keyExtractor.applyAsLong(l_value));
    }

    @NotNull
    default IntComparator thenComparingDouble(@NotNull IntToDoubleFunction keyExtractor) {
        return this.thenComparing(IntComparator.comparingDouble(keyExtractor));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #thenComparingDouble(IntToDoubleFunction)
     */
    @Deprecated
    @NotNull
    default Comparator<Integer> thenComparingDouble(@NotNull ToDoubleFunction<? super Integer> keyExtractor) {
        return this.thenComparingDouble((int l_value) -> keyExtractor.applyAsDouble(l_value));
    }

    @NotNull
    static <U> IntComparator comparing(@NotNull IntFunction<? extends U> keyExtractor, @NotNull Comparator<? super U> keyComparator) {
        Objects.requireNonNull(keyExtractor);
        Objects.requireNonNull(keyComparator);
        return (IntComparator & Serializable)
                (int l_c1, int l_c2) -> keyComparator.compare(keyExtractor.apply(l_c1), keyExtractor.apply(l_c2));
    }

    @NotNull
    static <U extends Comparable<? super U>> IntComparator comparing(@NotNull IntFunction<? extends U> keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (IntComparator & Serializable)
                (int l_c1, int l_c2) -> keyExtractor.apply(l_c1).compareTo(keyExtractor.apply(l_c2));
    }

    @NotNull
    static IntComparator comparingInt(@NotNull IntToIntFunction keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (IntComparator & Serializable)
                (int l_c1, int l_c2) -> Integer.compare(keyExtractor.apply(l_c1), keyExtractor.apply(l_c2));
    }

    @NotNull
    static IntComparator comparingLong(@NotNull IntToLongFunction keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (IntComparator & Serializable)
                (int l_c1, int l_c2) -> Long.compare(keyExtractor.apply(l_c1), keyExtractor.apply(l_c2));
    }

    @NotNull
    static IntComparator comparingDouble(@NotNull IntToDoubleFunction keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (IntComparator & Serializable)
                (int l_c1, int l_c2) -> Double.compare(keyExtractor.apply(l_c1), keyExtractor.apply(l_c2));
    }
}
