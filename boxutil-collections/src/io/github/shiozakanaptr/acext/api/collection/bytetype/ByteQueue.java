package io.github.shiozakanaptr.acext.api.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.QueueExt;
import io.github.shiozakanaptr.acext.util.function.bytetype.ByteConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.function.Consumer;

public interface ByteQueue extends QueueExt<Byte>, ByteCollection {
    byte peekRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #peekRaw()
     */
    @Deprecated
    default Byte peek() {
        return this.peekRaw();
    }

    default byte elementRaw() {
        byte result;
        if ((result = this.peekRaw()) == this.getNullElementRaw()) throw new NoSuchElementException();
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #elementRaw()
     */
    @Deprecated
    default Byte element() {
        return this.elementRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #add(byte)
     */
    @Deprecated
    default boolean add(Byte e) {
        return ByteCollection.super.add(e);
    }

    default boolean offer(byte e) {
        return this.add(e);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #offer(byte)
     */
    @Deprecated
    default boolean offer(Byte e) {
        return this.offer(e != null ? e.byteValue() : (byte) 0);
    }

    byte pollRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollRaw()
     */
    @Deprecated
    default Byte poll() {
        return this.pollRaw();
    }

    default byte removeRaw() {
        byte result;
        if ((result = this.pollRaw()) == this.getNullElementRaw()) throw new NoSuchElementException();
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeRaw()
     */
    @Deprecated
    default Byte remove() {
        return this.removeRaw();
    }

    boolean tryPollExecuteOffer(@NotNull ByteConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #tryPollExecuteOffer(ByteConsumer)
     */
    @Deprecated
    default boolean tryPollExecuteOffer(@NotNull Consumer<? super Byte> action) {
        return this.tryPollExecuteOffer((byte l_t) -> action.accept(l_t));
    }

    default byte getNullElementRaw() {
        return (byte) 0;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getNullElementRaw()
     */
    @Deprecated
    default Byte getNullElement() {
        return this.getNullElementRaw();
    }
}
