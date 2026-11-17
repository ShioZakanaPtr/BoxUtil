package io.github.shiozakanaptr.acext.api.collection.floattype;

import io.github.shiozakanaptr.acext.api.collection.doubletype.DoubleComparator;
import io.github.shiozakanaptr.acext.util.function.floattype.FloatFunction;
import io.github.shiozakanaptr.acext.util.function.floattype.FloatToIntFunction;
import io.github.shiozakanaptr.acext.util.function.floattype.FloatToLongFunction;
import io.github.shiozakanaptr.acext.util.function.floattype.FloatToDoubleFunction;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

@FunctionalInterface
public interface FloatComparator extends Comparator<Float> {
    int compare(float o1, float o2);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compare(Float, Float)
     */
    @Deprecated
    default int compare(Float o1, Float o2) {
        return this.compare(o1 != null ? o1.floatValue() : (float) 0, o2 != null ? o2.floatValue() : (float) 0);
    }

    @NotNull
    default FloatComparator reversed() {
        return new ReversedFloatComparator(this);
    }

    @NotNull
    default FloatComparator thenComparing(FloatComparator other) {
        Objects.requireNonNull(other);
        return (FloatComparator & Serializable) (float l_c1, float l_c2) -> {
            final int result;
            return (result = this.compare(l_c1, l_c2)) != 0 ? result : other.compare(l_c1, l_c2);
        };
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #thenComparing(FloatComparator)
     */
    @Deprecated
    @NotNull
    default Comparator<Float> thenComparing(@NotNull Comparator<? super Float> other) {
        return this.thenComparing((float l_o1, float l_o2) -> other.compare(l_o1, l_o2));
    }

    @NotNull
    default <U> FloatComparator thenComparing(@NotNull FloatFunction<? extends U> keyExtractor, Comparator<? super U> keyComparator) {
        return this.thenComparing(FloatComparator.comparing(keyExtractor, keyComparator));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #thenComparing(FloatFunction, Comparator)
     */
    @Deprecated
    @NotNull
    default <U> Comparator<Float> thenComparing(@NotNull Function<? super Float, ? extends U> keyExtractor, Comparator<? super U> keyComparator) {
        return this.thenComparing((float l_value) -> keyExtractor.apply(l_value), keyComparator);
    }

    @NotNull
    default <U extends Comparable<? super U>> FloatComparator thenComparing(@NotNull FloatFunction<? extends U> keyExtractor) {
        return this.thenComparing(FloatComparator.comparing(keyExtractor));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #thenComparing(FloatFunction)
     */
    @Deprecated
    @NotNull
    default <U extends Comparable<? super U>> Comparator<Float> thenComparing(@NotNull Function<? super Float, ? extends U> keyExtractor) {
        return this.thenComparing((float l_value) -> keyExtractor.apply(l_value));
    }

    @NotNull
    default FloatComparator thenComparingInt(@NotNull FloatToIntFunction keyExtractor) {
        return this.thenComparing(FloatComparator.comparingInt(keyExtractor));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #thenComparingInt(FloatToIntFunction)
     */
    @Deprecated
    @NotNull
    default Comparator<Float> thenComparingInt(@NotNull ToIntFunction<? super Float> keyExtractor) {
        return this.thenComparingInt((float l_value) -> keyExtractor.applyAsInt(l_value));
    }

    @NotNull
    default FloatComparator thenComparingLong(@NotNull FloatToLongFunction keyExtractor) {
        return this.thenComparing(FloatComparator.comparingLong(keyExtractor));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #thenComparingLong(FloatToLongFunction)
     */
    @Deprecated
    @NotNull
    default Comparator<Float> thenComparingLong(@NotNull ToLongFunction<? super Float> keyExtractor) {
        return this.thenComparingLong((float l_value) -> keyExtractor.applyAsLong(l_value));
    }

    @NotNull
    default FloatComparator thenComparingDouble(@NotNull FloatToDoubleFunction keyExtractor) {
        return this.thenComparing(FloatComparator.comparingDouble(keyExtractor));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #thenComparingDouble(FloatToDoubleFunction)
     */
    @Deprecated
    @NotNull
    default Comparator<Float> thenComparingDouble(@NotNull ToDoubleFunction<? super Float> keyExtractor) {
        return this.thenComparingDouble((float l_value) -> keyExtractor.applyAsDouble(l_value));
    }

    @NotNull
    static <U> FloatComparator comparing(@NotNull FloatFunction<? extends U> keyExtractor, @NotNull Comparator<? super U> keyComparator) {
        Objects.requireNonNull(keyExtractor);
        Objects.requireNonNull(keyComparator);
        return (FloatComparator & Serializable)
                (float l_c1, float l_c2) -> keyComparator.compare(keyExtractor.apply(l_c1), keyExtractor.apply(l_c2));
    }

    @NotNull
    static <U extends Comparable<? super U>> FloatComparator comparing(@NotNull FloatFunction<? extends U> keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (FloatComparator & Serializable)
                (float l_c1, float l_c2) -> keyExtractor.apply(l_c1).compareTo(keyExtractor.apply(l_c2));
    }

    @NotNull
    static FloatComparator comparingInt(@NotNull FloatToIntFunction keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (FloatComparator & Serializable)
                (float l_c1, float l_c2) -> Integer.compare(keyExtractor.apply(l_c1), keyExtractor.apply(l_c2));
    }

    @NotNull
    static FloatComparator comparingLong(@NotNull FloatToLongFunction keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (FloatComparator & Serializable)
                (float l_c1, float l_c2) -> Long.compare(keyExtractor.apply(l_c1), keyExtractor.apply(l_c2));
    }

    @NotNull
    static FloatComparator comparingDouble(@NotNull FloatToDoubleFunction keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (FloatComparator & Serializable)
                (float l_c1, float l_c2) -> Double.compare(keyExtractor.apply(l_c1), keyExtractor.apply(l_c2));
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
    static DoubleComparator wrap(@NotNull FloatComparator source) {
        return new FloatWrapComparator(source);
    }
}
