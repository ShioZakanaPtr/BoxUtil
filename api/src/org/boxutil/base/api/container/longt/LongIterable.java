package org.boxutil.base.api.container.longt;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

public interface LongIterable extends Iterable<Long> {
    @NotNull LongIterator iterator();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void forEach(Consumer<? super Long> action) {
        Objects.requireNonNull(action);
        forEach(action instanceof LongConsumer cast ? cast : action::accept);
    }

    default void forEach(LongConsumer action) {
        Objects.requireNonNull(action);
        this.iterator().forEachRemaining(action);
    }

    LongSpliterator spliterator();
}
