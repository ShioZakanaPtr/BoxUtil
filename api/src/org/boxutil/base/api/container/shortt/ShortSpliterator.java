package org.boxutil.base.api.container.shortt;

import org.boxutil.util.function.shortt.ShortConsumer;

import java.util.Spliterator;
import java.util.function.Consumer;

public interface ShortSpliterator extends Spliterator.OfPrimitive<Short, ShortConsumer, ShortSpliterator> {
    boolean tryAdvance(ShortConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean tryAdvance(Consumer<? super Short> action) {
        return this.tryAdvance(action::accept);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void forEachRemaining(Consumer<? super Short> action) {
        this.forEachRemaining(action::accept);
    }

    ShortSpliterator trySplit();

    default ShortComparator getComparator() {
        throw new IllegalStateException();
    }
}
