package io.github.shiozakanaptr.acext.api.collection.chartype;

import io.github.shiozakanaptr.acext.api.collection.BidiIterator;
import io.github.shiozakanaptr.acext.util.function.chartype.CharConsumer;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Type specialization interface.
 *
 * @see BidiIterator
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface CharBidiIterator extends CharIterator, BidiIterator<Character> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #nextChar()
     */
    @Deprecated
    default Character next() {
        return this.nextChar();
    }

    char previousChar();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #previousChar()
     */
    @Deprecated
    default Character previous() {
        return this.previousChar();
    }

    default void forEachPrevious(CharConsumer action) {
        Objects.requireNonNull(action);
        while (this.hasPrevious()) {
            action.accept(this.previousChar());
        }
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachPrevious(CharConsumer)
     */
    @Deprecated
    default void forEachPrevious(Consumer<? super Character> action) {
        this.forEachPrevious((char l_t) -> action.accept(l_t));
    }
}
