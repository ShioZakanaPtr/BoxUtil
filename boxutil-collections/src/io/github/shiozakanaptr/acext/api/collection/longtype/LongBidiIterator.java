package io.github.shiozakanaptr.acext.api.collection.longtype;

import io.github.shiozakanaptr.acext.api.collection.BidiIterator;
import java.util.function.LongConsumer;

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
public interface LongBidiIterator extends LongIterator, BidiIterator<Long> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #nextLong()
     */
    @Deprecated
    default Long next() {
        return this.nextLong();
    }

    long previousLong();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #previousLong()
     */
    @Deprecated
    default Long previous() {
        return this.previousLong();
    }

    default void forEachPrevious(LongConsumer action) {
        Objects.requireNonNull(action);
        while (this.hasPrevious()) {
            action.accept(this.previousLong());
        }
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachPrevious(LongConsumer)
     */
    @Deprecated
    default void forEachPrevious(Consumer<? super Long> action) {
        this.forEachPrevious((long l_t) -> action.accept(l_t));
    }
}
