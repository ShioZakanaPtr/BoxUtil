package io.github.shiozakanaptr.acext.api.collection.doubletype;

import io.github.shiozakanaptr.acext.api.collection.BidiIterator;
import java.util.function.DoubleConsumer;

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
public interface DoubleBidiIterator extends DoubleIterator, BidiIterator<Double> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #nextDouble()
     */
    @Deprecated
    default Double next() {
        return this.nextDouble();
    }

    double previousDouble();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #previousDouble()
     */
    @Deprecated
    default Double previous() {
        return this.previousDouble();
    }

    default void forEachPrevious(DoubleConsumer action) {
        Objects.requireNonNull(action);
        while (this.hasPrevious()) {
            action.accept(this.previousDouble());
        }
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachPrevious(DoubleConsumer)
     */
    @Deprecated
    default void forEachPrevious(Consumer<? super Double> action) {
        this.forEachPrevious((double l_t) -> action.accept(l_t));
    }
}
