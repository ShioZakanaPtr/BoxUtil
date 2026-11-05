package io.github.shiozakanaptr.acext.api.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.inttype.IntSpliterator;
import io.github.shiozakanaptr.acext.util.function.bytetype.ByteConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.Spliterator;
import java.util.function.Consumer;

public interface ByteSpliterator extends Spliterator.OfPrimitive<Byte, ByteConsumer, ByteSpliterator> {
    ByteSpliterator trySplit();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean tryAdvance(Consumer<? super Byte> action) {
        return this.tryAdvance((byte l_value) -> action.accept(l_value));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void forEachRemaining(Consumer<? super Byte> action) {
        this.forEachRemaining((byte l_value) -> action.accept(l_value));
    }

    default ByteComparator getComparator() {
        throw new IllegalStateException();
    }

    static @NotNull IntSpliterator wrap(final @NotNull ByteSpliterator source) {
        return new ByteWrapSpliterator(source);
    }
}
