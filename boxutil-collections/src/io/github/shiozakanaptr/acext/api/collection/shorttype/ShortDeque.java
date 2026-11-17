package io.github.shiozakanaptr.acext.api.collection.shorttype;

import io.github.shiozakanaptr.acext.api.collection.DequeExt;
import io.github.shiozakanaptr.acext.util.function.shorttype.ShortConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.function.Consumer;

public interface ShortDeque extends DequeExt<Short>, ShortQueue {
    default short peekRaw() {
        return this.peekFirstRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #peekRaw()
     */
    @Deprecated
    default Short peek() {
        return this.peekFirstRaw();
    }

    short peekFirstRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #peekFirstRaw()
     */
    @Deprecated
    default Short peekFirst() {
        return this.peekFirstRaw();
    }

    short peekLastRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #peekLastRaw()
     */
    @Deprecated
    default Short peekLast() {
        return this.peekLastRaw();
    }

    default short elementRaw() {
        return this.getFirstRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #elementRaw()
     */
    @Deprecated
    default Short element() {
        return this.getFirstRaw();
    }

    default short getFirstRaw() {
        short result;
        if ((result = this.peekFirstRaw()) == this.getNullElementRaw()) throw new NoSuchElementException();
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getFirstRaw()
     */
    @Deprecated
    default Short getFirst() {
        return this.getFirstRaw();
    }

    default short getLastRaw() {
        short result;
        if ((result = this.peekLastRaw()) == this.getNullElementRaw()) throw new NoSuchElementException();
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getLastRaw()
     */
    @Deprecated
    default Short getLast() {
        return this.getLastRaw();
    }

    default boolean add(short e) {
        this.addLast(e);
        return true;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #add(short)
     */
    @Deprecated
    default boolean add(Short e) {
        this.addLast(e != null ? e.shortValue() : (short) 0);
        return true;
    }

    void addFirst(short e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addFirst(short)
     */
    @Deprecated
    default void addFirst(Short e) {
        this.addFirst(e != null ? e.shortValue() : (short) 0);
    }

    void addLast(short e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addLast(short)
     */
    @Deprecated
    default void addLast(Short e) {
        this.addLast(e != null ? e.shortValue() : (short) 0);
    }

    default boolean offer(short e) {
        this.addLast(e);
        return true;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #offer(short)
     */
    @Deprecated
    default boolean offer(Short e) {
        this.addLast(e != null ? e.shortValue() : (short) 0);
        return true;
    }

    default boolean offerFirstRaw(short e) {
        this.addFirst(e);
        return true;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #offerFirstRaw(short)
     */
    @Deprecated
    default boolean offerFirst(Short e) {
        this.addFirst(e);
        return true;
    }

    default boolean offerLastRaw(short e) {
        this.addLast(e);
        return true;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #offerLastRaw(short)
     */
    @Deprecated
    default boolean offerLast(Short e) {
        this.addLast(e);
        return true;
    }

    default short pollRaw() {
        return this.pollFirstRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollRaw()
     */
    @Deprecated
    default Short poll() {
        return this.pollFirstRaw();
    }

    short pollFirstRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirstRaw()
     */
    @Deprecated
    default Short pollFirst() {
        return this.pollFirstRaw();
    }

    short pollLastRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLastRaw()
     */
    @Deprecated
    default Short pollLast() {
        return this.pollLastRaw();
    }

    default short removeRaw() {
        return this.removeFirstRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeRaw()
     */
    @Deprecated
    default Short remove() {
        return this.removeFirstRaw();
    }

    default short removeFirstRaw() {
        final short result;
        if ((result = this.pollFirstRaw()) == this.getNullElementRaw()) throw new NoSuchElementException();
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeFirstRaw()
     */
    @Deprecated
    default Short removeFirst() {
        return this.removeFirstRaw();
    }

    default short removeLastRaw() {
        final short result;
        if ((result = this.pollLastRaw()) == this.getNullElementRaw()) throw new NoSuchElementException();
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeLastRaw()
     */
    @Deprecated
    default Short removeLast() {
        return this.removeLastRaw();
    }

    /**
     * @deprecated Please use the corresponding method that renamed ones to
     * avoid overload-resolution ambiguity.
     * @see #removeElement(short)
     */
    @Deprecated
    default boolean remove(Object o) {
        return ShortQueue.super.remove(o);
    }

    boolean removeFirstOccurrence(short o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeFirstOccurrence(short)
     */
    @Deprecated
    default boolean removeFirstOccurrence(Object o) {
        if (o != null && !(o instanceof Short)) return false;
        return this.removeFirstOccurrence(o != null ? ((Short) o).shortValue() : (short) 0);
    }

    boolean removeLastOccurrence(short o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeLastOccurrence(short)
     */
    @Deprecated
    default boolean removeLastOccurrence(Object o) {
        if (o != null && !(o instanceof Short)) return false;
        return this.removeLastOccurrence(o != null ? ((Short) o).shortValue() : (short) 0);
    }

    default void push(short e) {
        this.addFirst(e);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #push(short)
     */
    @Deprecated
    default void push(Short e) {
        this.addFirst(e != null ? e.shortValue() : (short) 0);
    }

    default short popRaw() {
        return this.removeFirstRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #popRaw()
     */
    @Deprecated
    default Short pop() {
        return this.popRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #contains(short)
     */
    @Deprecated
    default boolean contains(Object o) {
        return ShortQueue.super.contains(o);
    }

    boolean tryPollExecuteOfferReversed(@NotNull ShortConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #tryPollExecuteOfferReversed(ShortConsumer)
     */
    @Deprecated
    default boolean tryPollExecuteOfferReversed(@NotNull Consumer<? super Short> action) {
        return this.tryPollExecuteOfferReversed((short l_t) -> action.accept(l_t));
    }

    @NotNull ShortIterator descendingIterator();
}
