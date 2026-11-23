package io.github.shiozakanaptr.acext.api.collection.floattype;

import io.github.shiozakanaptr.acext.api.collection.doubletype.DoubleComparator;
import io.github.shiozakanaptr.acext.util.function.doubletype.DoubleFunction;
import io.github.shiozakanaptr.acext.util.function.doubletype.DoubleToIntFunction;
import io.github.shiozakanaptr.acext.util.function.doubletype.DoubleToLongFunction;
import io.github.shiozakanaptr.acext.util.function.doubletype.DoubleToDoubleFunction;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

/**
 * Type specialization wrap class.
 *
 * @see FloatComparator
 * @see DoubleComparator
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
final class FloatWrapComparator implements DoubleComparator, Serializable {
    private static final long serialVersionUID = -7142995032377883752L;

    private final FloatComparator source;

    FloatWrapComparator(FloatComparator source) {
        this.source = Objects.requireNonNull(source);
    }

    public int compare(double o1, double o2) {
        return this.source.compare((float) o1, (float) o2);
    }

    public int compare(Double o1, Double o2) {
        return this.source.compare(o1 != null ? o1.floatValue() : (float) 0, o2 != null ? o2.floatValue() : (float) 0);
    }

    public DoubleComparator thenComparing(DoubleComparator other) {
        Objects.requireNonNull(other);
        return (DoubleComparator & Serializable) (double l_o1, double l_o2) -> {
            final int result;
            return (result = this.compare(l_o1, l_o2)) != 0 ? result : other.compare(l_o1, l_o2);
        };
    }

    public <U> DoubleComparator thenComparing(DoubleFunction<? extends U> keyExtractor, Comparator<? super U> keyComparator) {
        Objects.requireNonNull(keyExtractor);
        Objects.requireNonNull(keyComparator);
        return new FloatWrapComparator(this.source.thenComparing(FloatComparator.comparing((float l_t) -> keyExtractor.apply((double) l_t), keyComparator)));
    }

    public <U extends Comparable<? super U>> DoubleComparator thenComparing(DoubleFunction<? extends U> keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return new FloatWrapComparator(this.source.thenComparing(FloatComparator.comparing((float l_t) -> keyExtractor.apply((double) l_t))));
    }

    public DoubleComparator thenComparingInt(DoubleToIntFunction keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return new FloatWrapComparator(this.source.thenComparingInt((float l_t) -> keyExtractor.apply((double) l_t)));
    }

    public DoubleComparator thenComparingLong(DoubleToLongFunction keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return new FloatWrapComparator(this.source.thenComparingLong((float l_t) -> keyExtractor.apply((double) l_t)));
    }

    public DoubleComparator thenComparingDouble(DoubleToDoubleFunction keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return new FloatWrapComparator(this.source.thenComparingDouble((float l_t) -> keyExtractor.apply((double) l_t)));
    }

    public int hashCode() {
        return this.source.hashCode();
    }

    public boolean equals(Object o) {
        if (o instanceof FloatComparator) return this.source.equals(o);
        return o == this;
    }

    public String toString() {
        return this.source.toString();
    }
}
