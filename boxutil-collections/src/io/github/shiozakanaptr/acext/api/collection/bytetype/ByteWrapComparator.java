package io.github.shiozakanaptr.acext.api.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.inttype.IntComparator;
import io.github.shiozakanaptr.acext.util.function.inttype.IntFunction;
import io.github.shiozakanaptr.acext.util.function.inttype.IntToIntFunction;
import io.github.shiozakanaptr.acext.util.function.inttype.IntToLongFunction;
import io.github.shiozakanaptr.acext.util.function.inttype.IntToDoubleFunction;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

final class ByteWrapComparator implements IntComparator {
    private final ByteComparator source;

    ByteWrapComparator(ByteComparator source) {
        this.source = Objects.requireNonNull(source);
    }

    public int compare(int o1, int o2) {
        return this.source.compare((byte) o1, (byte) o2);
    }

    public int compare(Integer o1, Integer o2) {
        return this.source.compare(o1 != null ? o1.byteValue() : (byte) 0, o2 != null ? o2.byteValue() : (byte) 0);
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
        return new ByteWrapComparator(this.source.thenComparing(ByteComparator.comparing((byte l_t) -> keyExtractor.apply((int) l_t), keyComparator)));
    }

    public <U extends Comparable<? super U>> IntComparator thenComparing(IntFunction<? extends U> keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return new ByteWrapComparator(this.source.thenComparing(ByteComparator.comparing((byte l_t) -> keyExtractor.apply((int) l_t))));
    }

    public IntComparator thenComparingInt(IntToIntFunction keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return new ByteWrapComparator(this.source.thenComparingInt((byte l_t) -> keyExtractor.apply((int) l_t)));
    }

    public IntComparator thenComparingLong(IntToLongFunction keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return new ByteWrapComparator(this.source.thenComparingLong((byte l_t) -> keyExtractor.apply((int) l_t)));
    }

    public IntComparator thenComparingDouble(IntToDoubleFunction keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return new ByteWrapComparator(this.source.thenComparingDouble((byte l_t) -> keyExtractor.apply((int) l_t)));
    }

    public int hashCode() {
        return this.source.hashCode();
    }

    public boolean equals(Object o) {
        if (o instanceof ByteComparator) return this.source.equals(o);
        return o == this;
    }

    public String toString() {
        return this.source.toString();
    }
}
