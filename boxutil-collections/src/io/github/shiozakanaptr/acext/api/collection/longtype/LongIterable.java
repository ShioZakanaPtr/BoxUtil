package io.github.shiozakanaptr.acext.api.collection.longtype;

import java.util.function.LongConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Type specialization interface.
 *
 * @see Iterable
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface LongIterable extends Iterable<Long> {
    @NotNull LongIterator iterator();

    default void forEach(@NotNull LongConsumer action) {
        this.iterator().forEachRemaining(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(LongConsumer)
     */
    @Deprecated
    default void forEach(@NotNull Consumer<? super Long> action) {
        this.forEach((long l_t) -> action.accept(l_t));
    }

    LongSpliterator spliterator();
}
