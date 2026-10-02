package org.boxutil.base.api.container.longt;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.*;

public interface LongComparator extends Comparator<Long> {
    int compare(long o1, long o2);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default int compare(Long o1, Long o2) {
        return this.compare(o1.intValue(), o2.intValue());
    }

    @NotNull
    default LongComparator reversed() {
        return new ReversedLongComparator(this);
    }

    @NotNull
    default LongComparator thenComparing(LongComparator other) {
        Objects.requireNonNull(other);
        return (LongComparator & Serializable) (l_c1, l_c2) -> {
            int res = compare(l_c1, l_c2);
            return (res != 0) ? res : other.compare(l_c1, l_c2);
        };
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    @NotNull
    default Comparator<Long> thenComparing(Comparator<? super Long> other) {
        return this.thenComparing(other instanceof LongComparator cast ? cast : other::compare);
    }

    @NotNull
    default <U> LongComparator thenComparing(LongFunction<? extends U> keyExtractor, Comparator<? super U> keyComparator) {
        return this.thenComparing(LongComparator.comparing(keyExtractor, keyComparator));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    @NotNull
    default <U> Comparator<Long> thenComparing(Function<? super Long, ? extends U> keyExtractor, Comparator<? super U> keyComparator) {
        return this.thenComparing((long l_value) -> keyExtractor.apply(l_value), keyComparator);
    }

    @NotNull
    default <U extends Comparable<? super U>> LongComparator thenComparing(LongFunction<? extends U> keyExtractor) {
        return this.thenComparing(LongComparator.comparing(keyExtractor));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    @NotNull
    default <U extends Comparable<? super U>> Comparator<Long> thenComparing(Function<? super Long, ? extends U> keyExtractor) {
        return this.thenComparing((long l_value) -> keyExtractor.apply(l_value));
    }

    @NotNull
    default LongComparator thenComparingInt(LongToIntFunction keyExtractor) {
        return this.thenComparing(LongComparator.comparingInt(keyExtractor));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    @NotNull
    default Comparator<Long> thenComparingInt(ToIntFunction<? super Long> keyExtractor) {
        return this.thenComparingInt(keyExtractor instanceof LongToIntFunction cast ? cast : keyExtractor::applyAsInt);
    }

    @NotNull
    default LongComparator thenComparingLong(LongUnaryOperator keyExtractor) {
        return this.thenComparing(LongComparator.comparingLong(keyExtractor));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    @NotNull
    default Comparator<Long> thenComparingLong(ToLongFunction<? super Long> keyExtractor) {
        return this.thenComparingLong(keyExtractor instanceof LongUnaryOperator cast ? cast : keyExtractor::applyAsLong);
    }

    @NotNull
    default LongComparator thenComparingDouble(LongToDoubleFunction keyExtractor) {
        return this.thenComparing(LongComparator.comparingDouble(keyExtractor));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    @NotNull
    default Comparator<Long> thenComparingDouble(ToDoubleFunction<? super Long> keyExtractor) {
        return this.thenComparingDouble(keyExtractor instanceof LongToDoubleFunction cast ? cast : keyExtractor::applyAsDouble);
    }

    static <U> LongComparator comparing(
            LongFunction<? extends U> keyExtractor,
            Comparator<? super U> keyComparator)
    {
        Objects.requireNonNull(keyExtractor);
        Objects.requireNonNull(keyComparator);
        return (LongComparator & Serializable)
                (l_c1, l_c2) -> keyComparator.compare(keyExtractor.apply(l_c1), keyExtractor.apply(l_c2));
    }

    static <U extends Comparable<? super U>> LongComparator comparing(
            LongFunction<? extends U> keyExtractor)
    {
        Objects.requireNonNull(keyExtractor);
        return (LongComparator & Serializable)
                (l_c1, l_c2) -> keyExtractor.apply(l_c1).compareTo(keyExtractor.apply(l_c2));
    }

    static LongComparator comparingInt(LongToIntFunction keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (LongComparator & Serializable)
                (l_c1, l_c2) -> Integer.compare(keyExtractor.applyAsInt(l_c1), keyExtractor.applyAsInt(l_c2));
    }

    static LongComparator comparingLong(LongUnaryOperator keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (LongComparator & Serializable)
                (l_c1, l_c2) -> Long.compare(keyExtractor.applyAsLong(l_c1), keyExtractor.applyAsLong(l_c2));
    }

    static LongComparator comparingDouble(LongToDoubleFunction keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (LongComparator & Serializable)
                (l_c1, l_c2) -> Double.compare(keyExtractor.applyAsDouble(l_c1), keyExtractor.applyAsDouble(l_c2));
    }
}
