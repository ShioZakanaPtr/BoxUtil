package io.github.shiozakanaptr.acext.api.collection.bytetype;

import io.github.shiozakanaptr.acext.util.function.bytetype.ByteConsumer;
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
public interface ByteIterable extends Iterable<Byte> {
    @NotNull ByteIterator iterator();

    default void forEach(@NotNull ByteConsumer action) {
        this.iterator().forEachRemaining(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(ByteConsumer)
     */
    @Deprecated
    default void forEach(@NotNull Consumer<? super Byte> action) {
        this.forEach((byte l_t) -> action.accept(l_t));
    }

    ByteSpliterator spliterator();
}
