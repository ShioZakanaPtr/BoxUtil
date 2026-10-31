package org.boxutil.base.api.container.longtype;

import org.boxutil.base.api.container.DequeExt;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

public interface LongDeque extends DequeExt<Long>, LongQueue {
    default long peekRaw() {
        return this.peekFirstRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long peek() {
        return this.peekFirstRaw();
    }

    long peekFirstRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long peekFirst() {
        return this.peekFirstRaw();
    }

    long peekLastRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long peekLast() {
        return this.peekLastRaw();
    }

    default long elementRaw() {
        return this.getFirstRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long element() {
        return this.getFirstRaw();
    }

    default long getFirstRaw() {
        long result;
        if ((result = this.peekFirstRaw()) == this.getNullElementRaw()) throw new NoSuchElementException();
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long getFirst() {
        return this.getFirstRaw();
    }

    default long getLastRaw() {
        long result;
        if ((result = this.peekLastRaw()) == this.getNullElementRaw()) throw new NoSuchElementException();
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long getLast() {
        return this.getLastRaw();
    }

    default boolean add(long e) {
        this.addLast(e);
        return true;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean add(Long e) {
        this.addLast(e != null ? e.longValue() : (long) 0);
        return true;
    }

    void addFirst(long e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void addFirst(Long e) {
        this.addFirst(e != null ? e.longValue() : (long) 0);
    }

    void addLast(long e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void addLast(Long e) {
        this.addLast(e != null ? e.longValue() : (long) 0);
    }

    default boolean offer(long e) {
        this.addLast(e);
        return true;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean offer(Long e) {
        this.addLast(e != null ? e.longValue() : (long) 0);
        return true;
    }

    default boolean offerFirstRaw(long e) {
        this.addFirst(e);
        return true;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean offerFirst(Long e) {
        this.addFirst(e);
        return true;
    }

    default boolean offerLastRaw(long e) {
        this.addLast(e);
        return true;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean offerLast(Long e) {
        this.addLast(e);
        return true;
    }

    default long pollRaw() {
        return this.pollFirstRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long poll() {
        return this.pollFirstRaw();
    }

    long pollFirstRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long pollFirst() {
        return this.pollFirstRaw();
    }

    long pollLastRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long pollLast() {
        return this.pollLastRaw();
    }

    default long removeRaw() {
        return this.removeFirstRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long remove() {
        return this.removeFirstRaw();
    }

    default long removeFirstRaw() {
        final long result;
        if ((result = this.pollFirstRaw()) == this.getNullElementRaw()) throw new NoSuchElementException();
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long removeFirst() {
        return this.removeFirstRaw();
    }

    default long removeLastRaw() {
        final long result;
        if ((result = this.pollLastRaw()) == this.getNullElementRaw()) throw new NoSuchElementException();
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long removeLast() {
        return this.removeLastRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean remove(Object o) {
        return LongQueue.super.remove(o);
    }

    boolean removeFirstOccurrence(long o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean removeFirstOccurrence(Object o) {
        if (o != null && !(o instanceof Long)) return false;
        return this.removeFirstOccurrence(o != null ? ((Long) o).longValue() : (long) 0);
    }

    boolean removeLastOccurrence(long o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean removeLastOccurrence(Object o) {
        if (o != null && !(o instanceof Long)) return false;
        return this.removeLastOccurrence(o != null ? ((Long) o).longValue() : (long) 0);
    }

    default void push(long e) {
        this.addFirst(e);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void push(Long e) {
        this.addFirst(e != null ? e.longValue() : (long) 0);
    }

    default long popRaw() {
        return this.removeFirstRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long pop() {
        return this.popRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean contains(Object o) {
        return LongQueue.super.contains(o);
    }

    boolean tryPollExecuteOfferReversed(@NotNull LongConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean tryPollExecuteOfferReversed(@NotNull Consumer<? super Long> action) {
        return this.tryPollExecuteOfferReversed((long l_t) -> action.accept(l_t));
    }

    @NotNull LongIterator descendingIterator();
}
