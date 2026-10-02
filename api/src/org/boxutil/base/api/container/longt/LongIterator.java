package org.boxutil.base.api.container.longt;

import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

public interface LongIterator extends PrimitiveIterator.OfLong {
    long nextLong();

    default void forEachRemaining(LongConsumer action) {
        OfLong.super.forEachRemaining(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long next() {
        return this.nextLong();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void forEachRemaining(Consumer<? super Long> action) {
        Objects.requireNonNull(action);
        this.forEachRemaining(action instanceof LongConsumer cast ? cast : action::accept);
    }
}
