package org.boxutil.base.api.container.shorttype;

import org.boxutil.base.api.container.inttype.IntComparator;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.*;

final class ShortWrapComparator implements IntComparator {
    private final ShortComparator source;

    ShortWrapComparator(ShortComparator source) {
        this.source = source;
    }

    public int compare(int o1, int o2) {
        return this.source.compare((short) o1, (short) o2);
    }

    public int compare(Integer o1, Integer o2) {
        return this.source.compare(o1 != null ? o1.shortValue() : (short) 0, o2 != null ? o2.shortValue() : (short) 0);
    }

    public IntComparator thenComparing(IntComparator other) {
        Objects.requireNonNull(other);
        return (IntComparator & Serializable) (l_c1, l_c2) -> {
            int res = this.compare(l_c1, l_c2);
            return (res != 0) ? res : other.compare(l_c1, l_c2);
        };
    }

    public <U> IntComparator thenComparing(IntFunction<? extends U> keyExtractor, Comparator<? super U> keyComparator) {
        return new ShortWrapComparator(this.source.thenComparing(ShortComparator.comparing((short l_t) -> keyExtractor.apply(l_t), keyComparator)));
    }

    public <U extends Comparable<? super U>> IntComparator thenComparing(IntFunction<? extends U> keyExtractor) {
        return new ShortWrapComparator(this.source.thenComparing(ShortComparator.comparing((short l_t) -> keyExtractor.apply(l_t))));
    }

    public IntComparator thenComparingInt(IntUnaryOperator keyExtractor) {
        return new ShortWrapComparator(this.source.thenComparingInt((short l_t) -> keyExtractor.applyAsInt(l_t)));
    }

    public IntComparator thenComparingLong(IntToLongFunction keyExtractor) {
        return new ShortWrapComparator(this.source.thenComparingLong((short l_t) -> keyExtractor.applyAsLong(l_t)));
    }

    public IntComparator thenComparingDouble(IntToDoubleFunction keyExtractor) {
        return new ShortWrapComparator(this.source.thenComparingDouble((short l_t) -> keyExtractor.applyAsDouble(l_t)));
    }

    public int hashCode() {
        return this.source.hashCode();
    }

    public boolean equals(Object o) {
        if (o instanceof ShortComparator) return this.source.equals(o);
        return false;
    }

    public String toString() {
        return this.source.toString();
    }
}
