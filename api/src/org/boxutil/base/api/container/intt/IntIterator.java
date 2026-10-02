package org.boxutil.base.api.container.intt;

import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public interface IntIterator extends PrimitiveIterator.OfInt {
    int nextInt();

    default void forEachRemaining(IntConsumer action) {
        OfInt.super.forEachRemaining(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer next() {
        return this.nextInt();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void forEachRemaining(Consumer<? super Integer> action) {
        Objects.requireNonNull(action);
        this.forEachRemaining(action instanceof IntConsumer cast ? cast : action::accept);
    }
}
