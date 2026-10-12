package org.boxutil.base.api.container.intt;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public interface IntIterable extends Iterable<Integer> {
    @NotNull IntIterator iterator();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void forEach(Consumer<? super Integer> action) {
        Objects.requireNonNull(action);
        this.forEach((int l_value) -> action.accept(l_value));
    }

    default void forEach(IntConsumer action) {
        Objects.requireNonNull(action);
        this.iterator().forEachRemaining(action);
    }

    IntSpliterator spliterator();
}
