package org.boxutil.base.api.container.shorttype;

import org.boxutil.base.api.container.inttype.IntSpliterator;
import org.boxutil.util.function.shorttype.ShortConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.Spliterator;
import java.util.function.Consumer;

public interface ShortSpliterator extends Spliterator.OfPrimitive<Short, ShortConsumer, ShortSpliterator> {
    ShortSpliterator trySplit();

    boolean tryAdvance(ShortConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean tryAdvance(Consumer<? super Short> action) {
        return this.tryAdvance(action::accept);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void forEachRemaining(Consumer<? super Short> action) {
        this.forEachRemaining(action::accept);
    }

    default ShortComparator getComparator() {
        throw new IllegalStateException();
    }

    static @NotNull IntSpliterator wrap(final @NotNull ShortSpliterator source) {
        return new ShortWrapSpliterator(source);
    }
}
