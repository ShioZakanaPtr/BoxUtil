package io.github.shiozakanaptr.acext.api.collection.longtype;

import java.util.Spliterator;
import java.util.function.Consumer;

public interface LongSpliterator extends Spliterator.OfLong {
    LongSpliterator trySplit();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean tryAdvance(Consumer<? super Long> action) {
        return this.tryAdvance((long l_value) -> action.accept(l_value));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void forEachRemaining(Consumer<? super Long> action) {
        this.forEachRemaining((long l_value) -> action.accept(l_value));
    }

    default LongComparator getComparator() {
        throw new IllegalStateException();
    }
}
