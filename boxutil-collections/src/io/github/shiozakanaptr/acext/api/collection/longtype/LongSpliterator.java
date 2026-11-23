package io.github.shiozakanaptr.acext.api.collection.longtype;

import java.util.function.LongConsumer;
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
public interface LongSpliterator extends Spliterator.OfLong {
    LongSpliterator trySplit();

    boolean tryAdvance(@NotNull LongConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #tryAdvance(LongConsumer)
     */
    @Deprecated
    default boolean tryAdvance(@NotNull Consumer<? super Long> action) {
        return this.tryAdvance((long l_t) -> action.accept(l_t));
    }

    default void forEachRemaining(@NotNull LongConsumer action) {
        do {} while (this.tryAdvance(action));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachRemaining(LongConsumer)
     */
    @Deprecated
    default void forEachRemaining(@NotNull Consumer<? super Long> action) {
        this.forEachRemaining((long l_t) -> action.accept(l_t));
    }

    default LongComparator getComparator() {
        throw new IllegalStateException();
    }
}
