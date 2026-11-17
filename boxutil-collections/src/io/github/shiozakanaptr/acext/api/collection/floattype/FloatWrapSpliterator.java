package io.github.shiozakanaptr.acext.api.collection.floattype;

import io.github.shiozakanaptr.acext.api.collection.doubletype.DoubleComparator;
import io.github.shiozakanaptr.acext.api.collection.doubletype.DoubleSpliterator;

import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.Objects;

final class FloatWrapSpliterator implements DoubleSpliterator {
    private final FloatSpliterator source;

    FloatWrapSpliterator(FloatSpliterator source) {
        this.source = Objects.requireNonNull(source);
    }

    public FloatWrapSpliterator trySplit() {
        final FloatSpliterator newSplit;
        return (newSplit = this.source.trySplit()) != null ? new FloatWrapSpliterator(newSplit) : null;
    }

    public boolean tryAdvance(DoubleConsumer action) {
        Objects.requireNonNull(action);
        return this.source.tryAdvance((float l_t) -> action.accept((double) l_t));
    }

    public boolean tryAdvance(Consumer<? super Double> action) {
        Objects.requireNonNull(action);
        return this.source.tryAdvance((float l_t) -> action.accept((double) l_t));
    }

    public void forEachRemaining(DoubleConsumer action) {
        Objects.requireNonNull(action);
        this.source.forEachRemaining((float l_t) -> action.accept((double) l_t));
    }

    public void forEachRemaining(Consumer<? super Double> action) {
        Objects.requireNonNull(action);
        this.source.forEachRemaining((float l_t) -> action.accept((double) l_t));
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

    public DoubleComparator getComparator() {
        return FloatComparator.wrap(this.source.getComparator());
    }

    public int hashCode() {
        return this.source.hashCode();
    }

    public boolean equals(Object o) {
        if (o instanceof FloatSpliterator) return this.source.equals(o);
        return o == this;
    }

    public String toString() {
        return this.source.toString();
    }
}
