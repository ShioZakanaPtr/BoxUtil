package org.boxutil.base.api.container.shortt;

import org.boxutil.util.function.shortt.ShortConsumer;

import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;

public interface ShortIterator extends PrimitiveIterator<Short, ShortConsumer> {
    short nextShort();

    default void forEachRemaining(ShortConsumer action) {
        Objects.requireNonNull(action);
        while (this.hasNext()) {
            action.accept(this.nextShort());
        }
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short next() {
        return this.nextShort();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void forEachRemaining(Consumer<? super Short> action) {
        Objects.requireNonNull(action);
        this.forEachRemaining(action::accept);
    }
}
