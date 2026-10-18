package org.boxutil.base.api.container.shorttype;

import org.boxutil.util.function.shorttype.ShortConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

public interface ShortIterable extends Iterable<Short> {
    @NotNull ShortIterator iterator();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void forEach(Consumer<? super Short> action) {
        Objects.requireNonNull(action);
        this.forEach(action::accept);
    }

    default void forEach(ShortConsumer action) {
        Objects.requireNonNull(action);
        this.iterator().forEachRemaining(action);
    }

    ShortSpliterator spliterator();
}
