package io.github.shiozakanaptr.acext.api.collection.inttype;

import java.util.function.IntConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public interface IntIterable extends Iterable<Integer> {
    @NotNull IntIterator iterator();

    default void forEach(@NotNull IntConsumer action) {
        this.iterator().forEachRemaining(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(IntConsumer)
     */
    @Deprecated
    default void forEach(@NotNull Consumer<? super Integer> action) {
        this.forEach((int l_t) -> action.accept(l_t));
    }

    IntSpliterator spliterator();
}
