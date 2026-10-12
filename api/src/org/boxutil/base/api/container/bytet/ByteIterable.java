package org.boxutil.base.api.container.bytet;

import org.boxutil.util.function.bytet.ByteConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

public interface ByteIterable extends Iterable<Byte> {
    @NotNull ByteIterator iterator();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void forEach(Consumer<? super Byte> action) {
        Objects.requireNonNull(action);
        this.forEach(action::accept);
    }

    default void forEach(ByteConsumer action) {
        Objects.requireNonNull(action);
        this.iterator().forEachRemaining(action);
    }

    ByteSpliterator spliterator();
}
