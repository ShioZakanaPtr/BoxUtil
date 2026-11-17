package io.github.shiozakanaptr.acext.api.collection.shorttype;

import io.github.shiozakanaptr.acext.util.function.shorttype.ShortConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;
import java.util.function.Consumer;

public interface ShortIterator extends PrimitiveIterator<Short, ShortConsumer> {
    short nextShort();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #nextShort()
     */
    @Deprecated
    default Short next() {
        return this.nextShort();
    }

    default void forEachRemaining(@NotNull ShortConsumer action) {
        while (this.hasNext()) {
            action.accept(this.nextShort());
        }
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachRemaining(ShortConsumer)
     */
    @Deprecated
    default void forEachRemaining(@NotNull Consumer<? super Short> action) {
        this.forEachRemaining((short l_t) -> action.accept(l_t));
    }
}
