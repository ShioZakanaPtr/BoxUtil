package io.github.shiozakanaptr.acext.api.collection.shorttype;

import io.github.shiozakanaptr.acext.api.collection.inttype.IntSpliterator;
import io.github.shiozakanaptr.acext.util.function.shorttype.ShortConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.Spliterator;
import java.util.function.Consumer;

public interface ShortSpliterator extends Spliterator.OfPrimitive<Short, ShortConsumer, ShortSpliterator> {
    ShortSpliterator trySplit();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean tryAdvance(Consumer<? super Short> action) {
        return this.tryAdvance((short l_value) -> action.accept(l_value));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void forEachRemaining(Consumer<? super Short> action) {
        this.forEachRemaining((short l_value) -> action.accept(l_value));
    }

    default ShortComparator getComparator() {
        throw new IllegalStateException();
    }

    static @NotNull IntSpliterator wrap(final @NotNull ShortSpliterator source) {
        return new ShortWrapSpliterator(source);
    }
}
