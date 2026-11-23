package io.github.shiozakanaptr.acext.api.collection.longtype;

import io.github.shiozakanaptr.acext.util.function.longtype.LongFunction;
import io.github.shiozakanaptr.acext.util.function.longtype.LongToIntFunction;
import io.github.shiozakanaptr.acext.util.function.longtype.LongToLongFunction;
import io.github.shiozakanaptr.acext.util.function.longtype.LongToDoubleFunction;
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
public interface LongComparator extends Comparator<Long> {
    int compare(long o1, long o2);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compare(Long, Long)
     */
    @Deprecated
    default int compare(Long o1, Long o2) {
        return this.compare(o1 != null ? o1.longValue() : (long) 0, o2 != null ? o2.longValue() : (long) 0);
    }

    @NotNull
    default LongComparator reversed() {
        return new ReversedLongComparator(this);
    }

    @NotNull
    default LongComparator thenComparing(LongComparator other) {
        Objects.requireNonNull(other);
        return (LongComparator & Serializable) (long l_c1, long l_c2) -> {
            final int result;
            return (result = this.compare(l_c1, l_c2)) != 0 ? result : other.compare(l_c1, l_c2);
        };
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #thenComparing(LongComparator)
     */
    @Deprecated
    @NotNull
    default Comparator<Long> thenComparing(@NotNull Comparator<? super Long> other) {
        return this.thenComparing((long l_o1, long l_o2) -> other.compare(l_o1, l_o2));
    }

    @NotNull
    default <U> LongComparator thenComparing(@NotNull LongFunction<? extends U> keyExtractor, Comparator<? super U> keyComparator) {
        return this.thenComparing(LongComparator.comparing(keyExtractor, keyComparator));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #thenComparing(LongFunction, Comparator)
     */
    @Deprecated
    @NotNull
    default <U> Comparator<Long> thenComparing(@NotNull Function<? super Long, ? extends U> keyExtractor, Comparator<? super U> keyComparator) {
        return this.thenComparing((long l_value) -> keyExtractor.apply(l_value), keyComparator);
    }

    @NotNull
    default <U extends Comparable<? super U>> LongComparator thenComparing(@NotNull LongFunction<? extends U> keyExtractor) {
        return this.thenComparing(LongComparator.comparing(keyExtractor));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #thenComparing(LongFunction)
     */
    @Deprecated
    @NotNull
    default <U extends Comparable<? super U>> Comparator<Long> thenComparing(@NotNull Function<? super Long, ? extends U> keyExtractor) {
        return this.thenComparing((long l_value) -> keyExtractor.apply(l_value));
    }

    @NotNull
    default LongComparator thenComparingInt(@NotNull LongToIntFunction keyExtractor) {
        return this.thenComparing(LongComparator.comparingInt(keyExtractor));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #thenComparingInt(LongToIntFunction)
     */
    @Deprecated
    @NotNull
    default Comparator<Long> thenComparingInt(@NotNull ToIntFunction<? super Long> keyExtractor) {
        return this.thenComparingInt((long l_value) -> keyExtractor.applyAsInt(l_value));
    }

    @NotNull
    default LongComparator thenComparingLong(@NotNull LongToLongFunction keyExtractor) {
        return this.thenComparing(LongComparator.comparingLong(keyExtractor));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #thenComparingLong(LongToLongFunction)
     */
    @Deprecated
    @NotNull
    default Comparator<Long> thenComparingLong(@NotNull ToLongFunction<? super Long> keyExtractor) {
        return this.thenComparingLong((long l_value) -> keyExtractor.applyAsLong(l_value));
    }

    @NotNull
    default LongComparator thenComparingDouble(@NotNull LongToDoubleFunction keyExtractor) {
        return this.thenComparing(LongComparator.comparingDouble(keyExtractor));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #thenComparingDouble(LongToDoubleFunction)
     */
    @Deprecated
    @NotNull
    default Comparator<Long> thenComparingDouble(@NotNull ToDoubleFunction<? super Long> keyExtractor) {
        return this.thenComparingDouble((long l_value) -> keyExtractor.applyAsDouble(l_value));
    }

    @NotNull
    static <U> LongComparator comparing(@NotNull LongFunction<? extends U> keyExtractor, @NotNull Comparator<? super U> keyComparator) {
        Objects.requireNonNull(keyExtractor);
        Objects.requireNonNull(keyComparator);
        return (LongComparator & Serializable)
                (long l_c1, long l_c2) -> keyComparator.compare(keyExtractor.apply(l_c1), keyExtractor.apply(l_c2));
    }

    @NotNull
    static <U extends Comparable<? super U>> LongComparator comparing(@NotNull LongFunction<? extends U> keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (LongComparator & Serializable)
                (long l_c1, long l_c2) -> keyExtractor.apply(l_c1).compareTo(keyExtractor.apply(l_c2));
    }

    @NotNull
    static LongComparator comparingInt(@NotNull LongToIntFunction keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (LongComparator & Serializable)
                (long l_c1, long l_c2) -> Integer.compare(keyExtractor.apply(l_c1), keyExtractor.apply(l_c2));
    }

    @NotNull
    static LongComparator comparingLong(@NotNull LongToLongFunction keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (LongComparator & Serializable)
                (long l_c1, long l_c2) -> Long.compare(keyExtractor.apply(l_c1), keyExtractor.apply(l_c2));
    }

    @NotNull
    static LongComparator comparingDouble(@NotNull LongToDoubleFunction keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (LongComparator & Serializable)
                (long l_c1, long l_c2) -> Double.compare(keyExtractor.apply(l_c1), keyExtractor.apply(l_c2));
    }
}
