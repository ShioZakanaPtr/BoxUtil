package io.github.shiozakanaptr.acext.api.collection.shorttype;

import io.github.shiozakanaptr.acext.api.collection.inttype.IntComparator;
import io.github.shiozakanaptr.acext.util.function.inttype.IntFunction;
import io.github.shiozakanaptr.acext.util.function.inttype.IntToIntFunction;
import io.github.shiozakanaptr.acext.util.function.inttype.IntToLongFunction;
import io.github.shiozakanaptr.acext.util.function.inttype.IntToDoubleFunction;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

/**
 * Type specialization class.
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
final class ShortWrapComparator implements IntComparator, Serializable {
    private static final long serialVersionUID = -7142995032377883752L;

    private final ShortComparator source;

    ShortWrapComparator(ShortComparator source) {
        this.source = Objects.requireNonNull(source);
    }

    public int compare(int o1, int o2) {
        return this.source.compare((short) o1, (short) o2);
    }

    public int compare(Integer o1, Integer o2) {
        return this.source.compare(o1 != null ? o1.shortValue() : (short) 0, o2 != null ? o2.shortValue() : (short) 0);
    }

    public IntComparator thenComparing(IntComparator other) {
        Objects.requireNonNull(other);
        return (IntComparator & Serializable) (int l_o1, int l_o2) -> {
            final int result;
            return (result = this.compare(l_o1, l_o2)) != 0 ? result : other.compare(l_o1, l_o2);
        };
    }

    public <U> IntComparator thenComparing(IntFunction<? extends U> keyExtractor, Comparator<? super U> keyComparator) {
        Objects.requireNonNull(keyExtractor);
        Objects.requireNonNull(keyComparator);
        return new ShortWrapComparator(this.source.thenComparing(ShortComparator.comparing((short l_t) -> keyExtractor.apply((int) l_t), keyComparator)));
    }

    public <U extends Comparable<? super U>> IntComparator thenComparing(IntFunction<? extends U> keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return new ShortWrapComparator(this.source.thenComparing(ShortComparator.comparing((short l_t) -> keyExtractor.apply((int) l_t))));
    }

    public IntComparator thenComparingInt(IntToIntFunction keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return new ShortWrapComparator(this.source.thenComparingInt((short l_t) -> keyExtractor.apply((int) l_t)));
    }

    public IntComparator thenComparingLong(IntToLongFunction keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return new ShortWrapComparator(this.source.thenComparingLong((short l_t) -> keyExtractor.apply((int) l_t)));
    }

    public IntComparator thenComparingDouble(IntToDoubleFunction keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return new ShortWrapComparator(this.source.thenComparingDouble((short l_t) -> keyExtractor.apply((int) l_t)));
    }

    public int hashCode() {
        return this.source.hashCode();
    }

    public boolean equals(Object o) {
        if (o instanceof ShortComparator) return this.source.equals(o);
        return o == this;
    }

    public String toString() {
        return this.source.toString();
    }
}
