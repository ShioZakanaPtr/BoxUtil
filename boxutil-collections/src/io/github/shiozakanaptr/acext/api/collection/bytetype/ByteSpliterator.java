package io.github.shiozakanaptr.acext.api.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.inttype.IntSpliterator;
import io.github.shiozakanaptr.acext.util.function.bytetype.ByteConsumer;
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
public interface ByteSpliterator extends Spliterator.OfPrimitive<Byte, ByteConsumer, ByteSpliterator> {
    ByteSpliterator trySplit();

    boolean tryAdvance(@NotNull ByteConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #tryAdvance(ByteConsumer)
     */
    @Deprecated
    default boolean tryAdvance(@NotNull Consumer<? super Byte> action) {
        return this.tryAdvance((byte l_t) -> action.accept(l_t));
    }

    default void forEachRemaining(@NotNull ByteConsumer action) {
        do {} while (this.tryAdvance(action));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachRemaining(ByteConsumer)
     */
    @Deprecated
    default void forEachRemaining(@NotNull Consumer<? super Byte> action) {
        this.forEachRemaining((byte l_t) -> action.accept(l_t));
    }

    default ByteComparator getComparator() {
        throw new IllegalStateException();
    }

    /**
     * Returns a spliterator that wraps the given spliterator, adapting it for
     * consumption by a stream that requires a different element type.
     *
     * <p>The returned spliterator delegates traversal, splitting, and
     * characteristic reporting to the given spliterator.  Any elements produced
     * by the source are converted as required to match the element type expected
     * by the target stream implementation.
     *
     * @param source the spliterator to wrap; must not be {@code null}
     * @return a spliterator adapted for the target stream implementation
     * @throws NullPointerException if {@code source} is {@code null}
     */
    @NotNull
    static IntSpliterator wrap(@NotNull ByteSpliterator source) {
        return new ByteWrapSpliterator(source);
    }
}
