package io.github.shiozakanaptr.acext.api.collection.floattype;

import io.github.shiozakanaptr.acext.api.collection.BidiIterator;
import io.github.shiozakanaptr.acext.util.function.floattype.FloatConsumer;

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
public interface FloatBidiIterator extends FloatIterator, BidiIterator<Float> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #nextFloat()
     */
    @Deprecated
    default Float next() {
        return this.nextFloat();
    }

    float previousFloat();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #previousFloat()
     */
    @Deprecated
    default Float previous() {
        return this.previousFloat();
    }

    default void forEachPrevious(FloatConsumer action) {
        Objects.requireNonNull(action);
        while (this.hasPrevious()) {
            action.accept(this.previousFloat());
        }
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachPrevious(FloatConsumer)
     */
    @Deprecated
    default void forEachPrevious(Consumer<? super Float> action) {
        this.forEachPrevious((float l_t) -> action.accept(l_t));
    }
}
