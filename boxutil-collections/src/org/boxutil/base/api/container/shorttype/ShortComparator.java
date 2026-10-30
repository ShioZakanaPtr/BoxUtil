package org.boxutil.base.api.container.shorttype;

import org.boxutil.base.api.container.inttype.IntComparator;
import org.boxutil.util.function.shorttype.ShortFunction;
import org.boxutil.util.function.shorttype.ShortToDoubleFunction;
import org.boxutil.util.function.shorttype.ShortToIntFunction;
import org.boxutil.util.function.shorttype.ShortToLongFunction;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

public interface ShortComparator extends Comparator<Short> {
    int compare(short o1, short o2);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
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
        return (ShortComparator & Serializable) (l_c1, l_c2) -> {
            int res = compare(l_c1, l_c2);
            return (res != 0) ? res : other.compare(l_c1, l_c2);
        };
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    @NotNull
    default Comparator<Short> thenComparing(Comparator<? super Short> other) {
        return this.thenComparing(other::compare);
    }

    @NotNull
    default <U> ShortComparator thenComparing(ShortFunction<? extends U> keyExtractor, Comparator<? super U> keyComparator) {
        return this.thenComparing(ShortComparator.comparing(keyExtractor, keyComparator));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    @NotNull
    default <U> Comparator<Short> thenComparing(Function<? super Short, ? extends U> keyExtractor, Comparator<? super U> keyComparator) {
        return this.thenComparing((short l_value) -> keyExtractor.apply(l_value), keyComparator);
    }

    @NotNull
    default <U extends Comparable<? super U>> ShortComparator thenComparing(ShortFunction<? extends U> keyExtractor) {
        return this.thenComparing(ShortComparator.comparing(keyExtractor));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    @NotNull
    default <U extends Comparable<? super U>> Comparator<Short> thenComparing(Function<? super Short, ? extends U> keyExtractor) {
        return this.thenComparing((short l_value) -> keyExtractor.apply(l_value));
    }

    @NotNull
    default ShortComparator thenComparingInt(ShortToIntFunction keyExtractor) {
        return this.thenComparing(ShortComparator.comparingInt(keyExtractor));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    @NotNull
    default Comparator<Short> thenComparingInt(ToIntFunction<? super Short> keyExtractor) {
        return this.thenComparingInt((short l_value) -> keyExtractor.applyAsInt(l_value));
    }

    @NotNull
    default ShortComparator thenComparingLong(ShortToLongFunction keyExtractor) {
        return this.thenComparing(ShortComparator.comparingLong(keyExtractor));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    @NotNull
    default Comparator<Short> thenComparingLong(ToLongFunction<? super Short> keyExtractor) {
        return this.thenComparingLong((short l_value) -> keyExtractor.applyAsLong(l_value));
    }

    @NotNull
    default ShortComparator thenComparingDouble(ShortToDoubleFunction keyExtractor) {
        return this.thenComparing(ShortComparator.comparingDouble(keyExtractor));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    @NotNull
    default Comparator<Short> thenComparingDouble(ToDoubleFunction<? super Short> keyExtractor) {
        return this.thenComparingDouble((short l_value) -> keyExtractor.applyAsDouble(l_value));
    }

    static <U> ShortComparator comparing(
            ShortFunction<? extends U> keyExtractor,
            Comparator<? super U> keyComparator)
    {
        Objects.requireNonNull(keyExtractor);
        Objects.requireNonNull(keyComparator);
        return (ShortComparator & Serializable)
                (l_c1, l_c2) -> keyComparator.compare(keyExtractor.apply(l_c1), keyExtractor.apply(l_c2));
    }

    static <U extends Comparable<? super U>> ShortComparator comparing(
            ShortFunction<? extends U> keyExtractor)
    {
        Objects.requireNonNull(keyExtractor);
        return (ShortComparator & Serializable)
                (l_c1, l_c2) -> keyExtractor.apply(l_c1).compareTo(keyExtractor.apply(l_c2));
    }

    static ShortComparator comparingInt(ShortToIntFunction keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (ShortComparator & Serializable)
                (l_c1, l_c2) -> Integer.compare(keyExtractor.apply(l_c1), keyExtractor.apply(l_c2));
    }

    static ShortComparator comparingLong(ShortToLongFunction keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (ShortComparator & Serializable)
                (l_c1, l_c2) -> Long.compare(keyExtractor.apply(l_c1), keyExtractor.apply(l_c2));
    }

    static ShortComparator comparingDouble(ShortToDoubleFunction keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (ShortComparator & Serializable)
                (l_c1, l_c2) -> Double.compare(keyExtractor.apply(l_c1), keyExtractor.apply(l_c2));
    }

    static @NotNull IntComparator wrap(final @NotNull ShortComparator source) {
        return new ShortWrapComparator(source);
    }
}
