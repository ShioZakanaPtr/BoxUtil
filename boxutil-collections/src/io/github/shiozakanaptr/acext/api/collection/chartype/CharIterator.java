package io.github.shiozakanaptr.acext.api.collection.chartype;

import io.github.shiozakanaptr.acext.util.function.chartype.CharConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;
import java.util.function.Consumer;

public interface CharIterator extends PrimitiveIterator<Character, CharConsumer> {
    char nextChar();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #nextChar()
     */
    @Deprecated
    default Character next() {
        return this.nextChar();
    }

    default void forEachRemaining(@NotNull CharConsumer action) {
        while (this.hasNext()) {
            action.accept(this.nextChar());
        }
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachRemaining(CharConsumer)
     */
    @Deprecated
    default void forEachRemaining(@NotNull Consumer<? super Character> action) {
        this.forEachRemaining((char l_t) -> action.accept(l_t));
    }
}
