package io.github.shiozakanaptr.acext.api.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.inttype.IntComparator;
import io.github.shiozakanaptr.acext.api.collection.inttype.IntSpliterator;

import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.Objects;

/**
 * Type specialization class.
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
final class ByteWrapSpliterator implements IntSpliterator {
    private final ByteSpliterator source;

    ByteWrapSpliterator(ByteSpliterator source) {
        this.source = Objects.requireNonNull(source);
    }

    public ByteWrapSpliterator trySplit() {
        final ByteSpliterator newSplit;
        return (newSplit = this.source.trySplit()) != null ? new ByteWrapSpliterator(newSplit) : null;
    }

    public boolean tryAdvance(IntConsumer action) {
        Objects.requireNonNull(action);
        return this.source.tryAdvance((byte l_t) -> action.accept((int) l_t));
    }

    public boolean tryAdvance(Consumer<? super Integer> action) {
        Objects.requireNonNull(action);
        return this.source.tryAdvance((byte l_t) -> action.accept((int) l_t));
    }

    public void forEachRemaining(IntConsumer action) {
        Objects.requireNonNull(action);
        this.source.forEachRemaining((byte l_t) -> action.accept((int) l_t));
    }

    public void forEachRemaining(Consumer<? super Integer> action) {
        Objects.requireNonNull(action);
        this.source.forEachRemaining((byte l_t) -> action.accept((int) l_t));
    }

    public long getExactSizeIfKnown() {
        return this.source.getExactSizeIfKnown();
    }

    public boolean hasCharacteristics(int characteristics) {
        return this.source.hasCharacteristics(characteristics);
    }

    public long estimateSize() {
        return this.source.estimateSize();
    }

    public int characteristics() {
        return this.source.characteristics();
    }

    public IntComparator getComparator() {
        return ByteComparator.wrap(this.source.getComparator());
    }

    public int hashCode() {
        return this.source.hashCode();
    }

    public boolean equals(Object o) {
        if (o instanceof ByteSpliterator) return this.source.equals(o);
        return o == this;
    }

    public String toString() {
        return this.source.toString();
    }
}
