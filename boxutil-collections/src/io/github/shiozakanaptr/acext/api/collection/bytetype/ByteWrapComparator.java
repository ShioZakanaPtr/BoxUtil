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
        this.source = source;
    }

    public int compare(int o1, int o2) {
        return this.source.compare((byte) o1, (byte) o2);
    }

    public int compare(Integer o1, Integer o2) {
        return this.source.compare(o1 != null ? o1.byteValue() : (byte) 0, o2 != null ? o2.byteValue() : (byte) 0);
    }

    public IntComparator thenComparing(IntComparator other) {
        Objects.requireNonNull(other);
        return (IntComparator & Serializable) (l_c1, l_c2) -> {
            int res = this.compare(l_c1, l_c2);
            return (res != 0) ? res : other.compare(l_c1, l_c2);
        };
    }

    public <U> IntComparator thenComparing(IntFunction<? extends U> keyExtractor, Comparator<? super U> keyComparator) {
        return new ByteWrapComparator(this.source.thenComparing(ByteComparator.comparing((byte l_t) -> keyExtractor.apply((int) l_t), keyComparator)));
    }

    public <U extends Comparable<? super U>> IntComparator thenComparing(IntFunction<? extends U> keyExtractor) {
        return new ByteWrapComparator(this.source.thenComparing(ByteComparator.comparing((byte l_t) -> keyExtractor.apply((int) l_t))));
    }

    public IntComparator thenComparingInt(IntToIntFunction keyExtractor) {
        return new ByteWrapComparator(this.source.thenComparingInt((byte l_t) -> keyExtractor.apply((int) l_t)));
    }

    public IntComparator thenComparingLong(IntToLongFunction keyExtractor) {
        return new ByteWrapComparator(this.source.thenComparingLong((byte l_t) -> keyExtractor.apply((int) l_t)));
    }

    public IntComparator thenComparingDouble(IntToDoubleFunction keyExtractor) {
        return new ByteWrapComparator(this.source.thenComparingDouble((byte l_t) -> keyExtractor.apply((int) l_t)));
    }

    public int hashCode() {
        return this.source.hashCode();
    }

    public boolean equals(Object o) {
        if (o instanceof ByteComparator) return this.source.equals(o);
        return false;
    }

    public String toString() {
        return this.source.toString();
    }
}
