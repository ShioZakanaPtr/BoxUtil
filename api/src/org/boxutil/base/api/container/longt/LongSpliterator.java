package org.boxutil.base.api.container.longt;

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

public interface LongSpliterator extends Spliterator.OfLong {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean tryAdvance(Consumer<? super Long> action) {
        return this.tryAdvance(action instanceof LongConsumer cast ? cast : action::accept);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void forEachRemaining(Consumer<? super Long> action) {
        this.forEachRemaining(action instanceof LongConsumer cast ? cast : action::accept);
    }

    LongSpliterator trySplit();

    default LongComparator getComparator() {
        throw new IllegalStateException();
    }
}
