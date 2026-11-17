package io.github.shiozakanaptr.acext.api.collection.floattype;

import io.github.shiozakanaptr.acext.util.function.floattype.FloatConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public interface FloatIterable extends Iterable<Float> {
    @NotNull FloatIterator iterator();

    default void forEach(@NotNull FloatConsumer action) {
        this.iterator().forEachRemaining(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(FloatConsumer)
     */
    @Deprecated
    default void forEach(@NotNull Consumer<? super Float> action) {
        this.forEach((float l_t) -> action.accept(l_t));
    }

    FloatSpliterator spliterator();
}
