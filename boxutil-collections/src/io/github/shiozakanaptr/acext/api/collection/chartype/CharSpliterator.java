package io.github.shiozakanaptr.acext.api.collection.chartype;

import io.github.shiozakanaptr.acext.api.collection.inttype.IntSpliterator;
import io.github.shiozakanaptr.acext.util.function.chartype.CharConsumer;
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
public interface CharSpliterator extends Spliterator.OfPrimitive<Character, CharConsumer, CharSpliterator> {
    CharSpliterator trySplit();

    boolean tryAdvance(@NotNull CharConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #tryAdvance(CharConsumer)
     */
    @Deprecated
    default boolean tryAdvance(@NotNull Consumer<? super Character> action) {
        return this.tryAdvance((char l_t) -> action.accept(l_t));
    }

    default void forEachRemaining(@NotNull CharConsumer action) {
        do {} while (this.tryAdvance(action));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachRemaining(CharConsumer)
     */
    @Deprecated
    default void forEachRemaining(@NotNull Consumer<? super Character> action) {
        this.forEachRemaining((char l_t) -> action.accept(l_t));
    }

    default CharComparator getComparator() {
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
    static IntSpliterator wrap(@NotNull CharSpliterator source) {
        return new CharWrapSpliterator(source);
    }
}
