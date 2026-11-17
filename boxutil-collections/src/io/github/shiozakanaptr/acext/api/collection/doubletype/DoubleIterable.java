package io.github.shiozakanaptr.acext.api.collection.doubletype;

import java.util.function.DoubleConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public interface DoubleIterable extends Iterable<Double> {
    @NotNull DoubleIterator iterator();

    default void forEach(@NotNull DoubleConsumer action) {
        this.iterator().forEachRemaining(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(DoubleConsumer)
     */
    @Deprecated
    default void forEach(@NotNull Consumer<? super Double> action) {
        this.forEach((double l_t) -> action.accept(l_t));
    }

    DoubleSpliterator spliterator();
}
