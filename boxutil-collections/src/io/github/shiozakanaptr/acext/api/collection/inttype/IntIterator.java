package io.github.shiozakanaptr.acext.api.collection.inttype;

import java.util.function.IntConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;
import java.util.function.Consumer;

/**
 * Type specialization interface.
 *
 * @see java.util.Iterator
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface IntIterator extends PrimitiveIterator.OfInt {
    int nextInt();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #nextInt()
     */
    @Deprecated
    default Integer next() {
        return this.nextInt();
    }

    default void forEachRemaining(@NotNull IntConsumer action) {
        while (this.hasNext()) {
            action.accept(this.nextInt());
        }
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachRemaining(IntConsumer)
     */
    @Deprecated
    default void forEachRemaining(@NotNull Consumer<? super Integer> action) {
        this.forEachRemaining((int l_t) -> action.accept(l_t));
    }
}
