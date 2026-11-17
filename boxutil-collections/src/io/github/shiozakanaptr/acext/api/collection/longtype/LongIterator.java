package io.github.shiozakanaptr.acext.api.collection.longtype;

import java.util.function.LongConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;
import java.util.function.Consumer;

public interface LongIterator extends PrimitiveIterator.OfLong {
    long nextLong();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #nextLong()
     */
    @Deprecated
    default Long next() {
        return this.nextLong();
    }

    default void forEachRemaining(@NotNull LongConsumer action) {
        while (this.hasNext()) {
            action.accept(this.nextLong());
        }
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachRemaining(LongConsumer)
     */
    @Deprecated
    default void forEachRemaining(@NotNull Consumer<? super Long> action) {
        this.forEachRemaining((long l_t) -> action.accept(l_t));
    }
}
