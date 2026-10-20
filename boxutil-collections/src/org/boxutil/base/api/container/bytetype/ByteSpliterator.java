package org.boxutil.base.api.container.bytetype;

import org.boxutil.util.function.bytetype.ByteConsumer;

import java.util.Spliterator;
import java.util.function.Consumer;

public interface ByteSpliterator extends Spliterator.OfPrimitive<Byte, ByteConsumer, ByteSpliterator> {
    boolean tryAdvance(ByteConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean tryAdvance(Consumer<? super Byte> action) {
        return this.tryAdvance(action::accept);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void forEachRemaining(Consumer<? super Byte> action) {
        this.forEachRemaining(action::accept);
    }

    ByteSpliterator trySplit();

    default ByteComparator getComparator() {
        throw new IllegalStateException();
    }
}
