package io.github.shiozakanaptr.acext.api.collection.bytetype;

import io.github.shiozakanaptr.acext.util.function.bytetype.ByteConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;
import java.util.function.Consumer;

public interface ByteIterator extends PrimitiveIterator<Byte, ByteConsumer> {
    byte nextByte();

    default void forEachRemaining(@NotNull ByteConsumer action) {
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
    default void forEachRemaining(@NotNull Consumer<? super Byte> action) {
        this.forEachRemaining(action::accept);
    }
}
