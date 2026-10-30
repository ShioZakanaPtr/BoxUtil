package org.boxutil.base.api.container.bytetype;

import org.boxutil.base.api.container.inttype.IntComparator;
import org.boxutil.base.api.container.inttype.IntSpliterator;

import java.util.function.Consumer;
import java.util.function.IntConsumer;

final class ByteWrapSpliterator implements IntSpliterator {
    private final ByteSpliterator source;

    ByteWrapSpliterator(ByteSpliterator source) {
        this.source = source;
    }

    public ByteWrapSpliterator trySplit() {
        final ByteSpliterator newSplit;
        return (newSplit = this.source.trySplit()) != null ? new ByteWrapSpliterator(newSplit) : null;
    }

    public boolean tryAdvance(IntConsumer action) {
        return this.source.tryAdvance((byte l_t) -> action.accept(l_t));
    }

    public boolean tryAdvance(Consumer<? super Integer> action) {
        return this.source.tryAdvance((byte l_t) -> action.accept((int) l_t));
    }

    public void forEachRemaining(IntConsumer action) {
        this.source.forEachRemaining((byte l_t) -> action.accept(l_t));
    }

    public void forEachRemaining(Consumer<? super Integer> action) {
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

    public boolean equals(Object obj) {
        if (obj instanceof ByteSpliterator) return this.source.equals(obj);
        return false;
    }

    public String toString() {
        return this.source.toString();
    }
}
