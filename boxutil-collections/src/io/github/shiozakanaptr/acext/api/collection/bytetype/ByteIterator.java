package io.github.shiozakanaptr.acext.api.collection.bytetype;

import io.github.shiozakanaptr.acext.util.function.bytetype.ByteConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;
import java.util.function.Consumer;

/**
 * Type specialization interface.
 *
 * @see java.util.Iterator
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface ByteIterator extends PrimitiveIterator<Byte, ByteConsumer> {
    byte nextByte();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #nextByte()
     */
    @Deprecated
    default Byte next() {
        return this.nextByte();
    }

    default void forEachRemaining(@NotNull ByteConsumer action) {
        while (this.hasNext()) {
            action.accept(this.nextByte());
        }
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachRemaining(ByteConsumer)
     */
    @Deprecated
    default void forEachRemaining(@NotNull Consumer<? super Byte> action) {
        this.forEachRemaining((byte l_t) -> action.accept(l_t));
    }
}
