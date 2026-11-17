package io.github.shiozakanaptr.acext.api.collection.chartype;

import io.github.shiozakanaptr.acext.util.function.chartype.CharConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public interface CharIterable extends Iterable<Character> {
    @NotNull CharIterator iterator();

    default void forEach(@NotNull CharConsumer action) {
        this.iterator().forEachRemaining(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(CharConsumer)
     */
    @Deprecated
    default void forEach(@NotNull Consumer<? super Character> action) {
        this.forEach((char l_t) -> action.accept(l_t));
    }

    CharSpliterator spliterator();
}
