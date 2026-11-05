package io.github.shiozakanaptr.acext.api.collection.doubletype;

import java.util.Spliterator;
import java.util.function.Consumer;

public interface DoubleSpliterator extends Spliterator.OfDouble {
    DoubleSpliterator trySplit();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean tryAdvance(Consumer<? super Double> action) {
        return this.tryAdvance((double l_value) -> action.accept(l_value));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void forEachRemaining(Consumer<? super Double> action) {
        this.forEachRemaining((double l_value) -> action.accept(l_value));
    }

    default DoubleComparator getComparator() {
        throw new IllegalStateException();
    }
}
