package io.github.shiozakanaptr.acext.api.collection.inttype;

import org.jetbrains.annotations.NotNull;

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public interface IntSpliterator extends Spliterator.OfInt {
    IntSpliterator trySplit();

    boolean tryAdvance(@NotNull IntConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #tryAdvance(IntConsumer)
     */
    @Deprecated
    default boolean tryAdvance(@NotNull Consumer<? super Integer> action) {
        return this.tryAdvance((int l_value) -> action.accept(l_value));
    }

    default void forEachRemaining(@NotNull IntConsumer action) {
        do {} while (this.tryAdvance(action));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachRemaining(IntConsumer)
     */
    @Deprecated
    default void forEachRemaining(@NotNull Consumer<? super Integer> action) {
        this.forEachRemaining((int l_value) -> action.accept(l_value));
    }

    default IntComparator getComparator() {
        throw new IllegalStateException();
    }
}
