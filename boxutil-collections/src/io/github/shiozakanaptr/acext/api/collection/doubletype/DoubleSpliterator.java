package io.github.shiozakanaptr.acext.api.collection.doubletype;

import java.util.function.DoubleConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * Type specialization interface.
 *
 * @see Spliterator
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface DoubleSpliterator extends Spliterator.OfDouble {
    DoubleSpliterator trySplit();

    boolean tryAdvance(@NotNull DoubleConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #tryAdvance(DoubleConsumer)
     */
    @Deprecated
    default boolean tryAdvance(@NotNull Consumer<? super Double> action) {
        return this.tryAdvance((double l_t) -> action.accept(l_t));
    }

    default void forEachRemaining(@NotNull DoubleConsumer action) {
        do {} while (this.tryAdvance(action));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachRemaining(DoubleConsumer)
     */
    @Deprecated
    default void forEachRemaining(@NotNull Consumer<? super Double> action) {
        this.forEachRemaining((double l_t) -> action.accept(l_t));
    }

    default DoubleComparator getComparator() {
        throw new IllegalStateException();
    }
}
