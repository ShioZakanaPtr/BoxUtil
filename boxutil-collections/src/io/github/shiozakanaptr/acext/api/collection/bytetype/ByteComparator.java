package io.github.shiozakanaptr.acext.api.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.inttype.IntComparator;
import io.github.shiozakanaptr.acext.util.function.bytetype.ByteFunction;
import io.github.shiozakanaptr.acext.util.function.bytetype.ByteToIntFunction;
import io.github.shiozakanaptr.acext.util.function.bytetype.ByteToLongFunction;
import io.github.shiozakanaptr.acext.util.function.bytetype.ByteToDoubleFunction;
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
public interface ByteComparator extends Comparator<Byte> {
    int compare(byte o1, byte o2);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compare(Byte, Byte)
     */
    @Deprecated
    default int compare(Byte o1, Byte o2) {
        return this.compare(o1 != null ? o1.byteValue() : (byte) 0, o2 != null ? o2.byteValue() : (byte) 0);
    }

    @NotNull
    default ByteComparator reversed() {
        return new ReversedByteComparator(this);
    }

    @NotNull
    default ByteComparator thenComparing(ByteComparator other) {
        Objects.requireNonNull(other);
        return (ByteComparator & Serializable) (byte l_c1, byte l_c2) -> {
            final int result;
            return (result = this.compare(l_c1, l_c2)) != 0 ? result : other.compare(l_c1, l_c2);
        };
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #thenComparing(ByteComparator)
     */
    @Deprecated
    @NotNull
    default Comparator<Byte> thenComparing(@NotNull Comparator<? super Byte> other) {
        return this.thenComparing((byte l_o1, byte l_o2) -> other.compare(l_o1, l_o2));
    }

    @NotNull
    default <U> ByteComparator thenComparing(@NotNull ByteFunction<? extends U> keyExtractor, Comparator<? super U> keyComparator) {
        return this.thenComparing(ByteComparator.comparing(keyExtractor, keyComparator));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #thenComparing(ByteFunction, Comparator)
     */
    @Deprecated
    @NotNull
    default <U> Comparator<Byte> thenComparing(@NotNull Function<? super Byte, ? extends U> keyExtractor, Comparator<? super U> keyComparator) {
        return this.thenComparing((byte l_value) -> keyExtractor.apply(l_value), keyComparator);
    }

    @NotNull
    default <U extends Comparable<? super U>> ByteComparator thenComparing(@NotNull ByteFunction<? extends U> keyExtractor) {
        return this.thenComparing(ByteComparator.comparing(keyExtractor));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #thenComparing(ByteFunction)
     */
    @Deprecated
    @NotNull
    default <U extends Comparable<? super U>> Comparator<Byte> thenComparing(@NotNull Function<? super Byte, ? extends U> keyExtractor) {
        return this.thenComparing((byte l_value) -> keyExtractor.apply(l_value));
    }

    @NotNull
    default ByteComparator thenComparingInt(@NotNull ByteToIntFunction keyExtractor) {
        return this.thenComparing(ByteComparator.comparingInt(keyExtractor));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #thenComparingInt(ByteToIntFunction)
     */
    @Deprecated
    @NotNull
    default Comparator<Byte> thenComparingInt(@NotNull ToIntFunction<? super Byte> keyExtractor) {
        return this.thenComparingInt((byte l_value) -> keyExtractor.applyAsInt(l_value));
    }

    @NotNull
    default ByteComparator thenComparingLong(@NotNull ByteToLongFunction keyExtractor) {
        return this.thenComparing(ByteComparator.comparingLong(keyExtractor));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #thenComparingLong(ByteToLongFunction)
     */
    @Deprecated
    @NotNull
    default Comparator<Byte> thenComparingLong(@NotNull ToLongFunction<? super Byte> keyExtractor) {
        return this.thenComparingLong((byte l_value) -> keyExtractor.applyAsLong(l_value));
    }

    @NotNull
    default ByteComparator thenComparingDouble(@NotNull ByteToDoubleFunction keyExtractor) {
        return this.thenComparing(ByteComparator.comparingDouble(keyExtractor));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #thenComparingDouble(ByteToDoubleFunction)
     */
    @Deprecated
    @NotNull
    default Comparator<Byte> thenComparingDouble(@NotNull ToDoubleFunction<? super Byte> keyExtractor) {
        return this.thenComparingDouble((byte l_value) -> keyExtractor.applyAsDouble(l_value));
    }

    @NotNull
    static <U> ByteComparator comparing(@NotNull ByteFunction<? extends U> keyExtractor, @NotNull Comparator<? super U> keyComparator) {
        Objects.requireNonNull(keyExtractor);
        Objects.requireNonNull(keyComparator);
        return (ByteComparator & Serializable)
                (byte l_c1, byte l_c2) -> keyComparator.compare(keyExtractor.apply(l_c1), keyExtractor.apply(l_c2));
    }

    @NotNull
    static <U extends Comparable<? super U>> ByteComparator comparing(@NotNull ByteFunction<? extends U> keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (ByteComparator & Serializable)
                (byte l_c1, byte l_c2) -> keyExtractor.apply(l_c1).compareTo(keyExtractor.apply(l_c2));
    }

    @NotNull
    static ByteComparator comparingInt(@NotNull ByteToIntFunction keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (ByteComparator & Serializable)
                (byte l_c1, byte l_c2) -> Integer.compare(keyExtractor.apply(l_c1), keyExtractor.apply(l_c2));
    }

    @NotNull
    static ByteComparator comparingLong(@NotNull ByteToLongFunction keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (ByteComparator & Serializable)
                (byte l_c1, byte l_c2) -> Long.compare(keyExtractor.apply(l_c1), keyExtractor.apply(l_c2));
    }

    @NotNull
    static ByteComparator comparingDouble(@NotNull ByteToDoubleFunction keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (ByteComparator & Serializable)
                (byte l_c1, byte l_c2) -> Double.compare(keyExtractor.apply(l_c1), keyExtractor.apply(l_c2));
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
    static IntComparator wrap(@NotNull ByteComparator source) {
        return new ByteWrapComparator(source);
    }
}
