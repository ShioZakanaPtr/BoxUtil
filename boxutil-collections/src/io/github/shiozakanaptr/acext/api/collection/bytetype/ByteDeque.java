package io.github.shiozakanaptr.acext.api.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.DequeExt;
import io.github.shiozakanaptr.acext.util.function.bytetype.ByteConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.function.Consumer;

public interface ByteDeque extends DequeExt<Byte>, ByteQueue {
    default byte peekRaw() {
        return this.peekFirstRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #peekRaw()
     */
    @Deprecated
    default Byte peek() {
        return this.peekFirstRaw();
    }

    byte peekFirstRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #peekFirstRaw()
     */
    @Deprecated
    default Byte peekFirst() {
        return this.peekFirstRaw();
    }

    byte peekLastRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #peekLastRaw()
     */
    @Deprecated
    default Byte peekLast() {
        return this.peekLastRaw();
    }

    default byte elementRaw() {
        return this.getFirstRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #elementRaw()
     */
    @Deprecated
    default Byte element() {
        return this.getFirstRaw();
    }

    default byte getFirstRaw() {
        byte result;
        if ((result = this.peekFirstRaw()) == this.getNullElementRaw()) throw new NoSuchElementException();
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getFirstRaw()
     */
    @Deprecated
    default Byte getFirst() {
        return this.getFirstRaw();
    }

    default byte getLastRaw() {
        byte result;
        if ((result = this.peekLastRaw()) == this.getNullElementRaw()) throw new NoSuchElementException();
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getLastRaw()
     */
    @Deprecated
    default Byte getLast() {
        return this.getLastRaw();
    }

    default boolean add(byte e) {
        this.addLast(e);
        return true;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #add(byte)
     */
    @Deprecated
    default boolean add(Byte e) {
        this.addLast(e != null ? e.byteValue() : (byte) 0);
        return true;
    }

    void addFirst(byte e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addFirst(byte)
     */
    @Deprecated
    default void addFirst(Byte e) {
        this.addFirst(e != null ? e.byteValue() : (byte) 0);
    }

    void addLast(byte e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addLast(byte)
     */
    @Deprecated
    default void addLast(Byte e) {
        this.addLast(e != null ? e.byteValue() : (byte) 0);
    }

    default boolean offer(byte e) {
        this.addLast(e);
        return true;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #offer(byte)
     */
    @Deprecated
    default boolean offer(Byte e) {
        this.addLast(e != null ? e.byteValue() : (byte) 0);
        return true;
    }

    default boolean offerFirstRaw(byte e) {
        this.addFirst(e);
        return true;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #offerFirstRaw(byte)
     */
    @Deprecated
    default boolean offerFirst(Byte e) {
        this.addFirst(e);
        return true;
    }

    default boolean offerLastRaw(byte e) {
        this.addLast(e);
        return true;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #offerLastRaw(byte)
     */
    @Deprecated
    default boolean offerLast(Byte e) {
        this.addLast(e);
        return true;
    }

    default byte pollRaw() {
        return this.pollFirstRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollRaw()
     */
    @Deprecated
    default Byte poll() {
        return this.pollFirstRaw();
    }

    byte pollFirstRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirstRaw()
     */
    @Deprecated
    default Byte pollFirst() {
        return this.pollFirstRaw();
    }

    byte pollLastRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLastRaw()
     */
    @Deprecated
    default Byte pollLast() {
        return this.pollLastRaw();
    }

    default byte removeRaw() {
        return this.removeFirstRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeRaw()
     */
    @Deprecated
    default Byte remove() {
        return this.removeFirstRaw();
    }

    default byte removeFirstRaw() {
        final byte result;
        if ((result = this.pollFirstRaw()) == this.getNullElementRaw()) throw new NoSuchElementException();
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeFirstRaw()
     */
    @Deprecated
    default Byte removeFirst() {
        return this.removeFirstRaw();
    }

    default byte removeLastRaw() {
        final byte result;
        if ((result = this.pollLastRaw()) == this.getNullElementRaw()) throw new NoSuchElementException();
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeLastRaw()
     */
    @Deprecated
    default Byte removeLast() {
        return this.removeLastRaw();
    }

    /**
     * @deprecated Please use the corresponding method that renamed ones to
     * avoid overload-resolution ambiguity.
     * @see #removeElement(byte)
     */
    @Deprecated
    default boolean remove(Object o) {
        return ByteQueue.super.remove(o);
    }

    boolean removeFirstOccurrence(byte o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeFirstOccurrence(byte)
     */
    @Deprecated
    default boolean removeFirstOccurrence(Object o) {
        if (o != null && !(o instanceof Byte)) return false;
        return this.removeFirstOccurrence(o != null ? ((Byte) o).byteValue() : (byte) 0);
    }

    boolean removeLastOccurrence(byte o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeLastOccurrence(byte)
     */
    @Deprecated
    default boolean removeLastOccurrence(Object o) {
        if (o != null && !(o instanceof Byte)) return false;
        return this.removeLastOccurrence(o != null ? ((Byte) o).byteValue() : (byte) 0);
    }

    default void push(byte e) {
        this.addFirst(e);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #push(byte)
     */
    @Deprecated
    default void push(Byte e) {
        this.addFirst(e != null ? e.byteValue() : (byte) 0);
    }

    default byte popRaw() {
        return this.removeFirstRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #popRaw()
     */
    @Deprecated
    default Byte pop() {
        return this.popRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #contains(byte)
     */
    @Deprecated
    default boolean contains(Object o) {
        return ByteQueue.super.contains(o);
    }

    boolean tryPollExecuteOfferReversed(@NotNull ByteConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #tryPollExecuteOfferReversed(ByteConsumer)
     */
    @Deprecated
    default boolean tryPollExecuteOfferReversed(@NotNull Consumer<? super Byte> action) {
        return this.tryPollExecuteOfferReversed((byte l_t) -> action.accept(l_t));
    }

    @NotNull ByteIterator descendingIterator();
}
