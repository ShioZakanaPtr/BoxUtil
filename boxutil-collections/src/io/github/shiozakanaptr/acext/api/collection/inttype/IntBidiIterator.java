package io.github.shiozakanaptr.acext.api.collection.inttype;

import io.github.shiozakanaptr.acext.api.collection.BidiIterator;
import java.util.function.IntConsumer;

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
public interface IntBidiIterator extends IntIterator, BidiIterator<Integer> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #nextInt()
     */
    @Deprecated
    default Integer next() {
        return this.nextInt();
    }

    int previousInt();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #previousInt()
     */
    @Deprecated
    default Integer previous() {
        return this.previousInt();
    }

    default void forEachPrevious(IntConsumer action) {
        Objects.requireNonNull(action);
        while (this.hasPrevious()) {
            action.accept(this.previousInt());
        }
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachPrevious(IntConsumer)
     */
    @Deprecated
    default void forEachPrevious(Consumer<? super Integer> action) {
        this.forEachPrevious((int l_t) -> action.accept(l_t));
    }
}
