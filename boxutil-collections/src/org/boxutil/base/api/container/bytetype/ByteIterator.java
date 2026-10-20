package org.boxutil.base.api.container.bytetype;

import org.boxutil.util.function.bytetype.ByteConsumer;

import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;

public interface ByteIterator extends PrimitiveIterator<Byte, ByteConsumer> {
    byte nextByte();

    default void forEachRemaining(ByteConsumer action) {
        Objects.requireNonNull(action);
        while (this.hasNext()) {
            action.accept(this.nextByte());
        }
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Byte next() {
        return this.nextByte();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void forEachRemaining(Consumer<? super Byte> action) {
        Objects.requireNonNull(action);
        this.forEachRemaining(action::accept);
    }
}
