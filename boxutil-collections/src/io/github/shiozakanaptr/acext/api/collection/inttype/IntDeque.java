package io.github.shiozakanaptr.acext.api.collection.inttype;

import io.github.shiozakanaptr.acext.api.collection.DequeExt;
import java.util.function.IntConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.function.Consumer;

/**
 * Type specialization interface.
 *
 * @see DequeExt
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface IntDeque extends DequeExt<Integer>, IntQueue {
    default int peekRaw() {
        return this.peekFirstRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #peekRaw()
     */
    @Deprecated
    default Integer peek() {
        return this.peekFirstRaw();
    }

    int peekFirstRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #peekFirstRaw()
     */
    @Deprecated
    default Integer peekFirst() {
        return this.peekFirstRaw();
    }

    int peekLastRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #peekLastRaw()
     */
    @Deprecated
    default Integer peekLast() {
        return this.peekLastRaw();
    }

    default int elementRaw() {
        return this.getFirstRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #elementRaw()
     */
    @Deprecated
    default Integer element() {
        return this.getFirstRaw();
    }

    default int getFirstRaw() {
        int result;
        if ((result = this.peekFirstRaw()) == this.getNullElementRaw()) throw new NoSuchElementException();
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getFirstRaw()
     */
    @Deprecated
    default Integer getFirst() {
        return this.getFirstRaw();
    }

    default int getLastRaw() {
        int result;
        if ((result = this.peekLastRaw()) == this.getNullElementRaw()) throw new NoSuchElementException();
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getLastRaw()
     */
    @Deprecated
    default Integer getLast() {
        return this.getLastRaw();
    }

    default boolean add(int e) {
        this.addLast(e);
        return true;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #add(int)
     */
    @Deprecated
    default boolean add(Integer e) {
        this.addLast(e != null ? e.intValue() : (int) 0);
        return true;
    }

    void addFirst(int e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addFirst(int)
     */
    @Deprecated
    default void addFirst(Integer e) {
        this.addFirst(e != null ? e.intValue() : (int) 0);
    }

    void addLast(int e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addLast(int)
     */
    @Deprecated
    default void addLast(Integer e) {
        this.addLast(e != null ? e.intValue() : (int) 0);
    }

    default boolean offer(int e) {
        this.addLast(e);
        return true;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #offer(int)
     */
    @Deprecated
    default boolean offer(Integer e) {
        this.addLast(e != null ? e.intValue() : (int) 0);
        return true;
    }

    default boolean offerFirstRaw(int e) {
        this.addFirst(e);
        return true;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #offerFirstRaw(int)
     */
    @Deprecated
    default boolean offerFirst(Integer e) {
        this.addFirst(e);
        return true;
    }

    default boolean offerLastRaw(int e) {
        this.addLast(e);
        return true;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #offerLastRaw(int)
     */
    @Deprecated
    default boolean offerLast(Integer e) {
        this.addLast(e);
        return true;
    }

    default int pollRaw() {
        return this.pollFirstRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollRaw()
     */
    @Deprecated
    default Integer poll() {
        return this.pollFirstRaw();
    }

    int pollFirstRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirstRaw()
     */
    @Deprecated
    default Integer pollFirst() {
        return this.pollFirstRaw();
    }

    int pollLastRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLastRaw()
     */
    @Deprecated
    default Integer pollLast() {
        return this.pollLastRaw();
    }

    default int removeRaw() {
        return this.removeFirstRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeRaw()
     */
    @Deprecated
    default Integer remove() {
        return this.removeFirstRaw();
    }

    default int removeFirstRaw() {
        final int result;
        if ((result = this.pollFirstRaw()) == this.getNullElementRaw()) throw new NoSuchElementException();
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeFirstRaw()
     */
    @Deprecated
    default Integer removeFirst() {
        return this.removeFirstRaw();
    }

    default int removeLastRaw() {
        final int result;
        if ((result = this.pollLastRaw()) == this.getNullElementRaw()) throw new NoSuchElementException();
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeLastRaw()
     */
    @Deprecated
    default Integer removeLast() {
        return this.removeLastRaw();
    }

    boolean removeFirstOccurrence(int o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeFirstOccurrence(int)
     */
    @Deprecated
    default boolean removeFirstOccurrence(Object o) {
        if (o != null && !(o instanceof Integer)) return false;
        return this.removeFirstOccurrence(o != null ? ((Integer) o).intValue() : (int) 0);
    }

    boolean removeLastOccurrence(int o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeLastOccurrence(int)
     */
    @Deprecated
    default boolean removeLastOccurrence(Object o) {
        if (o != null && !(o instanceof Integer)) return false;
        return this.removeLastOccurrence(o != null ? ((Integer) o).intValue() : (int) 0);
    }

    default void push(int e) {
        this.addFirst(e);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #push(int)
     */
    @Deprecated
    default void push(Integer e) {
        this.addFirst(e != null ? e.intValue() : (int) 0);
    }

    default int popRaw() {
        return this.removeFirstRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #popRaw()
     */
    @Deprecated
    default Integer pop() {
        return this.popRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #contains(int)
     */
    @Deprecated
    default boolean contains(Object o) {
        return IntQueue.super.contains(o);
    }

    boolean tryPollExecuteOfferReversed(@NotNull IntConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #tryPollExecuteOfferReversed(IntConsumer)
     */
    @Deprecated
    default boolean tryPollExecuteOfferReversed(@NotNull Consumer<? super Integer> action) {
        return this.tryPollExecuteOfferReversed((int l_t) -> action.accept(l_t));
    }

    @NotNull
    IntBidiIterator iterator();

    @NotNull
    IntBidiIterator iteratorReversed();

    @NotNull IntIterator descendingIterator();
}
