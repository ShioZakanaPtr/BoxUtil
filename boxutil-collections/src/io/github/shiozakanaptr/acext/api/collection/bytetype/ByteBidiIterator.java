package io.github.shiozakanaptr.acext.api.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.BidiIterator;
import io.github.shiozakanaptr.acext.util.function.bytetype.ByteConsumer;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Type specialization interface.
 *
 * @see BidiIterator
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface ByteBidiIterator extends ByteIterator, BidiIterator<Byte> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #nextByte()
     */
    @Deprecated
    default Byte next() {
        return this.nextByte();
    }

    byte previousByte();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #previousByte()
     */
    @Deprecated
    default Byte previous() {
        return this.previousByte();
    }

    default void forEachPrevious(ByteConsumer action) {
        Objects.requireNonNull(action);
        while (this.hasPrevious()) {
            action.accept(this.previousByte());
        }
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachPrevious(ByteConsumer)
     */
    @Deprecated
    default void forEachPrevious(Consumer<? super Byte> action) {
        this.forEachPrevious((byte l_t) -> action.accept(l_t));
    }
}
