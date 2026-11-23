package io.github.shiozakanaptr.acext.api.collection.floattype;

import io.github.shiozakanaptr.acext.api.collection.doubletype.DoubleSpliterator;
import io.github.shiozakanaptr.acext.util.function.floattype.FloatConsumer;
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
public interface FloatSpliterator extends Spliterator.OfPrimitive<Float, FloatConsumer, FloatSpliterator> {
    FloatSpliterator trySplit();

    boolean tryAdvance(@NotNull FloatConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #tryAdvance(FloatConsumer)
     */
    @Deprecated
    default boolean tryAdvance(@NotNull Consumer<? super Float> action) {
        return this.tryAdvance((float l_t) -> action.accept(l_t));
    }

    default void forEachRemaining(@NotNull FloatConsumer action) {
        do {} while (this.tryAdvance(action));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachRemaining(FloatConsumer)
     */
    @Deprecated
    default void forEachRemaining(@NotNull Consumer<? super Float> action) {
        this.forEachRemaining((float l_t) -> action.accept(l_t));
    }

    default FloatComparator getComparator() {
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
    static DoubleSpliterator wrap(@NotNull FloatSpliterator source) {
        return new FloatWrapSpliterator(source);
    }
}
