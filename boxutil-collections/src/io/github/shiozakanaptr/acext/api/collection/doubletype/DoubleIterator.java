package io.github.shiozakanaptr.acext.api.collection.doubletype;

import java.util.function.DoubleConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;
import java.util.function.Consumer;

public interface DoubleIterator extends PrimitiveIterator.OfDouble {
    double nextDouble();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #nextDouble()
     */
    @Deprecated
    default Double next() {
        return this.nextDouble();
    }

    default void forEachRemaining(@NotNull DoubleConsumer action) {
        while (this.hasNext()) {
            action.accept(this.nextDouble());
        }
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachRemaining(DoubleConsumer)
     */
    @Deprecated
    default void forEachRemaining(@NotNull Consumer<? super Double> action) {
        this.forEachRemaining((double l_t) -> action.accept(l_t));
    }
}
