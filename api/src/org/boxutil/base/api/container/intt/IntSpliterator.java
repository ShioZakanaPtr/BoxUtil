package org.boxutil.base.api.container.intt;

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public interface IntSpliterator extends Spliterator.OfInt {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean tryAdvance(Consumer<? super Integer> action) {
        return this.tryAdvance(action instanceof IntConsumer cast ? cast : action::accept);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void forEachRemaining(Consumer<? super Integer> action) {
        this.forEachRemaining(action instanceof IntConsumer cast ? cast : action::accept);
    }

    IntSpliterator trySplit();

    default IntComparator getComparator() {
        throw new IllegalStateException();
    }
}
