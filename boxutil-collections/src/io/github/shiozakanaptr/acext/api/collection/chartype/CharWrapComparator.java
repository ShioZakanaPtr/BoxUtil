package io.github.shiozakanaptr.acext.api.collection.chartype;

import io.github.shiozakanaptr.acext.api.collection.inttype.IntComparator;
import io.github.shiozakanaptr.acext.util.function.inttype.IntFunction;
import io.github.shiozakanaptr.acext.util.function.inttype.IntToIntFunction;
import io.github.shiozakanaptr.acext.util.function.inttype.IntToLongFunction;
import io.github.shiozakanaptr.acext.util.function.inttype.IntToDoubleFunction;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

/**
 * Type specialization wrap class.
 *
 * @see CharComparator
 * @see IntComparator
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
final class CharWrapComparator implements IntComparator, Serializable {
    private static final long serialVersionUID = -7142995032377883752L;

    private final CharComparator source;

    CharWrapComparator(CharComparator source) {
        this.source = Objects.requireNonNull(source);
    }

    public int compare(int o1, int o2) {
        return this.source.compare((char) o1, (char) o2);
    }

    public int compare(Integer o1, Integer o2) {
        return this.source.compare(o1 != null ? (char) o1.intValue() : (char) 0, o2 != null ? (char) o2.intValue() : (char) 0);
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
        return new CharWrapComparator(this.source.thenComparing(CharComparator.comparing((char l_t) -> keyExtractor.apply((int) l_t), keyComparator)));
    }

    public <U extends Comparable<? super U>> IntComparator thenComparing(IntFunction<? extends U> keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return new CharWrapComparator(this.source.thenComparing(CharComparator.comparing((char l_t) -> keyExtractor.apply((int) l_t))));
    }

    public IntComparator thenComparingInt(IntToIntFunction keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return new CharWrapComparator(this.source.thenComparingInt((char l_t) -> keyExtractor.apply((int) l_t)));
    }

    public IntComparator thenComparingLong(IntToLongFunction keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return new CharWrapComparator(this.source.thenComparingLong((char l_t) -> keyExtractor.apply((int) l_t)));
    }

    public IntComparator thenComparingDouble(IntToDoubleFunction keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return new CharWrapComparator(this.source.thenComparingDouble((char l_t) -> keyExtractor.apply((int) l_t)));
    }

    public int hashCode() {
        return this.source.hashCode();
    }

    public boolean equals(Object o) {
        if (o instanceof CharComparator) return this.source.equals(o);
        return o == this;
    }

    public String toString() {
        return this.source.toString();
    }
}
