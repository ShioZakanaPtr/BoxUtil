package org.boxutil.base.api.container.inttype;

import org.boxutil.util.function.inttype.IntToDoubleFunction;
import org.boxutil.util.function.inttype.IntToLongFunction;
import org.boxutil.util.function.inttype.IntUnaryOperator;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.*;

public interface IntComparator extends Comparator<Integer> {
    int compare(int o1, int o2);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
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
            int res = this.compare(l_c1, l_c2);
            return (res != 0) ? res : other.compare(l_c1, l_c2);
        };
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    @NotNull
    default Comparator<Integer> thenComparing(Comparator<? super Integer> other) {
        return this.thenComparing(other::compare);
    }

    @NotNull
    default <U> IntComparator thenComparing(IntFunction<? extends U> keyExtractor, Comparator<? super U> keyComparator) {
        return this.thenComparing(IntComparator.comparing(keyExtractor, keyComparator));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    @NotNull
    default <U> Comparator<Integer> thenComparing(Function<? super Integer, ? extends U> keyExtractor, Comparator<? super U> keyComparator) {
        return this.thenComparing((int l_value) -> keyExtractor.apply(l_value), keyComparator);
    }

    @NotNull
    default <U extends Comparable<? super U>> IntComparator thenComparing(IntFunction<? extends U> keyExtractor) {
        return this.thenComparing(IntComparator.comparing(keyExtractor));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    @NotNull
    default <U extends Comparable<? super U>> Comparator<Integer> thenComparing(Function<? super Integer, ? extends U> keyExtractor) {
        return this.thenComparing((int l_value) -> keyExtractor.apply(l_value));
    }

    @NotNull
    default IntComparator thenComparingInt(IntUnaryOperator keyExtractor) {
        return this.thenComparing(IntComparator.comparingInt(keyExtractor));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    @NotNull
    default Comparator<Integer> thenComparingInt(ToIntFunction<? super Integer> keyExtractor) {
        return this.thenComparingInt((int l_value) -> keyExtractor.applyAsInt(l_value));
    }

    @NotNull
    default IntComparator thenComparingLong(IntToLongFunction keyExtractor) {
        return this.thenComparing(IntComparator.comparingLong(keyExtractor));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    @NotNull
    default Comparator<Integer> thenComparingLong(ToLongFunction<? super Integer> keyExtractor) {
        return this.thenComparingLong((int l_value) -> keyExtractor.applyAsLong(l_value));
    }

    @NotNull
    default IntComparator thenComparingDouble(IntToDoubleFunction keyExtractor) {
        return this.thenComparing(IntComparator.comparingDouble(keyExtractor));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    @NotNull
    default Comparator<Integer> thenComparingDouble(ToDoubleFunction<? super Integer> keyExtractor) {
        return this.thenComparingDouble((int l_value) -> keyExtractor.applyAsDouble(l_value));
    }

    static <U> IntComparator comparing(
            IntFunction<? extends U> keyExtractor,
            Comparator<? super U> keyComparator)
    {
        Objects.requireNonNull(keyExtractor);
        Objects.requireNonNull(keyComparator);
        return (IntComparator & Serializable)
                (l_c1, l_c2) -> keyComparator.compare(keyExtractor.apply(l_c1), keyExtractor.apply(l_c2));
    }

    static <U extends Comparable<? super U>> IntComparator comparing(
            IntFunction<? extends U> keyExtractor)
    {
        Objects.requireNonNull(keyExtractor);
        return (IntComparator & Serializable)
                (l_c1, l_c2) -> keyExtractor.apply(l_c1).compareTo(keyExtractor.apply(l_c2));
    }

    static IntComparator comparingInt(IntUnaryOperator keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (IntComparator & Serializable)
                (l_c1, l_c2) -> Integer.compare(keyExtractor.apply(l_c1), keyExtractor.apply(l_c2));
    }

    static IntComparator comparingLong(IntToLongFunction keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (IntComparator & Serializable)
                (l_c1, l_c2) -> Long.compare(keyExtractor.apply(l_c1), keyExtractor.apply(l_c2));
    }

    static IntComparator comparingDouble(IntToDoubleFunction keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (IntComparator & Serializable)
                (l_c1, l_c2) -> Double.compare(keyExtractor.apply(l_c1), keyExtractor.apply(l_c2));
    }
}
