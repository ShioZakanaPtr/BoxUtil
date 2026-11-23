package io.github.shiozakanaptr.acext.api.collection.shorttype;

import io.github.shiozakanaptr.acext.api.collection.BidiIterator;
import io.github.shiozakanaptr.acext.util.function.shorttype.ShortConsumer;

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
public interface ShortBidiIterator extends ShortIterator, BidiIterator<Short> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #nextShort()
     */
    @Deprecated
    default Short next() {
        return this.nextShort();
    }

    short previousShort();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #previousShort()
     */
    @Deprecated
    default Short previous() {
        return this.previousShort();
    }

    default void forEachPrevious(ShortConsumer action) {
        Objects.requireNonNull(action);
        while (this.hasPrevious()) {
            action.accept(this.previousShort());
        }
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachPrevious(ShortConsumer)
     */
    @Deprecated
    default void forEachPrevious(Consumer<? super Short> action) {
        this.forEachPrevious((short l_t) -> action.accept(l_t));
    }
}
