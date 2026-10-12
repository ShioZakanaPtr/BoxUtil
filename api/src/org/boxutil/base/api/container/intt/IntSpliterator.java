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
        return this.tryAdvance((int l_value) -> action.accept(l_value));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void forEachRemaining(Consumer<? super Integer> action) {
        this.forEachRemaining((int l_value) -> action.accept(l_value));
    }

    IntSpliterator trySplit();

    default IntComparator getComparator() {
        throw new IllegalStateException();
    }
}
