package io.github.shiozakanaptr.acext.api.collection.shorttype;

import io.github.shiozakanaptr.acext.api.collection.inttype.IntSpliterator;
import io.github.shiozakanaptr.acext.util.function.shorttype.ShortConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.Spliterator;
import java.util.function.Consumer;

public interface ShortSpliterator extends Spliterator.OfPrimitive<Short, ShortConsumer, ShortSpliterator> {
    ShortSpliterator trySplit();

    boolean tryAdvance(@NotNull ShortConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #tryAdvance(ShortConsumer)
     */
    @Deprecated
    default boolean tryAdvance(@NotNull Consumer<? super Short> action) {
        return this.tryAdvance((short l_t) -> action.accept(l_t));
    }

    default void forEachRemaining(@NotNull ShortConsumer action) {
        do {} while (this.tryAdvance(action));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachRemaining(ShortConsumer)
     */
    @Deprecated
    default void forEachRemaining(@NotNull Consumer<? super Short> action) {
        this.forEachRemaining((short l_t) -> action.accept(l_t));
    }

    default ShortComparator getComparator() {
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
    static IntSpliterator wrap(@NotNull ShortSpliterator source) {
        return new ShortWrapSpliterator(source);
    }
}
