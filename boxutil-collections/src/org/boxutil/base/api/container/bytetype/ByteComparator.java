package org.boxutil.base.api.container.bytetype;

import org.boxutil.util.function.bytetype.ByteFunction;
import org.boxutil.util.function.bytetype.ByteToDoubleFunction;
import org.boxutil.util.function.bytetype.ByteToIntFunction;
import org.boxutil.util.function.bytetype.ByteToLongFunction;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.*;

public interface ByteComparator extends Comparator<Byte> {
    int compare(byte o1, byte o2);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default int compare(Byte o1, Byte o2) {
        return this.compare(o1.byteValue(), o2.byteValue());
    }

    @NotNull
    default ByteComparator reversed() {
        return new ReversedByteComparator(this);
    }

    @NotNull
    default ByteComparator thenComparing(ByteComparator other) {
        Objects.requireNonNull(other);
        return (ByteComparator & Serializable) (l_c1, l_c2) -> {
            int res = compare(l_c1, l_c2);
            return (res != 0) ? res : other.compare(l_c1, l_c2);
        };
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    @NotNull
    default Comparator<Byte> thenComparing(Comparator<? super Byte> other) {
        return this.thenComparing(other::compare);
    }

    @NotNull
    default <U> ByteComparator thenComparing(ByteFunction<? extends U> keyExtractor, Comparator<? super U> keyComparator) {
        return this.thenComparing(ByteComparator.comparing(keyExtractor, keyComparator));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    @NotNull
    default <U> Comparator<Byte> thenComparing(Function<? super Byte, ? extends U> keyExtractor, Comparator<? super U> keyComparator) {
        return this.thenComparing(keyExtractor::apply, keyComparator);
    }

    @NotNull
    default <U extends Comparable<? super U>> ByteComparator thenComparing(ByteFunction<? extends U> keyExtractor) {
        return this.thenComparing(ByteComparator.comparing(keyExtractor));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    @NotNull
    default <U extends Comparable<? super U>> Comparator<Byte> thenComparing(Function<? super Byte, ? extends U> keyExtractor) {
        return this.thenComparing(keyExtractor::apply);
    }

    @NotNull
    default ByteComparator thenComparingInt(ByteToIntFunction keyExtractor) {
        return this.thenComparing(ByteComparator.comparingInt(keyExtractor));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    @NotNull
    default Comparator<Byte> thenComparingInt(ToIntFunction<? super Byte> keyExtractor) {
        return this.thenComparingInt((byte l_value) -> keyExtractor.applyAsInt(l_value));
    }

    @NotNull
    default ByteComparator thenComparingLong(ByteToLongFunction keyExtractor) {
        return this.thenComparing(ByteComparator.comparingLong(keyExtractor));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    @NotNull
    default Comparator<Byte> thenComparingLong(ToLongFunction<? super Byte> keyExtractor) {
        return this.thenComparingLong((byte l_value) -> keyExtractor.applyAsLong(l_value));
    }

    @NotNull
    default ByteComparator thenComparingDouble(ByteToDoubleFunction keyExtractor) {
        return this.thenComparing(ByteComparator.comparingDouble(keyExtractor));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    @NotNull
    default Comparator<Byte> thenComparingDouble(ToDoubleFunction<? super Byte> keyExtractor) {
        return this.thenComparingDouble((byte l_value) -> keyExtractor.applyAsDouble(l_value));
    }

    static <U> ByteComparator comparing(
            ByteFunction<? extends U> keyExtractor,
            Comparator<? super U> keyComparator)
    {
        Objects.requireNonNull(keyExtractor);
        Objects.requireNonNull(keyComparator);
        return (ByteComparator & Serializable)
                (l_c1, l_c2) -> keyComparator.compare(keyExtractor.apply(l_c1), keyExtractor.apply(l_c2));
    }

    static <U extends Comparable<? super U>> ByteComparator comparing(
            ByteFunction<? extends U> keyExtractor)
    {
        Objects.requireNonNull(keyExtractor);
        return (ByteComparator & Serializable)
                (l_c1, l_c2) -> keyExtractor.apply(l_c1).compareTo(keyExtractor.apply(l_c2));
    }

    static ByteComparator comparingInt(ByteToIntFunction keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (ByteComparator & Serializable)
                (l_c1, l_c2) -> Integer.compare(keyExtractor.apply(l_c1), keyExtractor.apply(l_c2));
    }

    static ByteComparator comparingLong(ByteToLongFunction keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (ByteComparator & Serializable)
                (l_c1, l_c2) -> Long.compare(keyExtractor.apply(l_c1), keyExtractor.apply(l_c2));
    }

    static ByteComparator comparingDouble(ByteToDoubleFunction keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (ByteComparator & Serializable)
                (l_c1, l_c2) -> Double.compare(keyExtractor.apply(l_c1), keyExtractor.apply(l_c2));
    }
}
