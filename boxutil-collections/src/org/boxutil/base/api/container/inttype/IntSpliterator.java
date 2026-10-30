package org.boxutil.base.api.container.inttype;

import java.util.Spliterator;
import java.util.function.Consumer;

public interface IntSpliterator extends Spliterator.OfInt {
    IntSpliterator trySplit();

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

    default IntComparator getComparator() {
        throw new IllegalStateException();
    }
}
