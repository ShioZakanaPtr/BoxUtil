package io.github.shiozakanaptr.acext.api.collection.shorttype;

import io.github.shiozakanaptr.acext.util.function.shorttype.ShortConsumer;
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
public interface ShortIterable extends Iterable<Short> {
    @NotNull ShortIterator iterator();

    default void forEach(@NotNull ShortConsumer action) {
        this.iterator().forEachRemaining(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(ShortConsumer)
     */
    @Deprecated
    default void forEach(@NotNull Consumer<? super Short> action) {
        this.forEach((short l_t) -> action.accept(l_t));
    }

    ShortSpliterator spliterator();
}
