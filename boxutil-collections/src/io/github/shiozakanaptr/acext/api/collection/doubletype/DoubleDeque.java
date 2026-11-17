package io.github.shiozakanaptr.acext.api.collection.doubletype;

import io.github.shiozakanaptr.acext.api.collection.DequeExt;
import java.util.function.DoubleConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.function.Consumer;

public interface DoubleDeque extends DequeExt<Double>, DoubleQueue {
    default double peekRaw() {
        return this.peekFirstRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #peekRaw()
     */
    @Deprecated
    default Double peek() {
        return this.peekFirstRaw();
    }

    double peekFirstRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #peekFirstRaw()
     */
    @Deprecated
    default Double peekFirst() {
        return this.peekFirstRaw();
    }

    double peekLastRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #peekLastRaw()
     */
    @Deprecated
    default Double peekLast() {
        return this.peekLastRaw();
    }

    default double elementRaw() {
        return this.getFirstRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #elementRaw()
     */
    @Deprecated
    default Double element() {
        return this.getFirstRaw();
    }

    default double getFirstRaw() {
        double result;
        if ((result = this.peekFirstRaw()) == this.getNullElementRaw()) throw new NoSuchElementException();
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getFirstRaw()
     */
    @Deprecated
    default Double getFirst() {
        return this.getFirstRaw();
    }

    default double getLastRaw() {
        double result;
        if ((result = this.peekLastRaw()) == this.getNullElementRaw()) throw new NoSuchElementException();
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getLastRaw()
     */
    @Deprecated
    default Double getLast() {
        return this.getLastRaw();
    }

    default boolean add(double e) {
        this.addLast(e);
        return true;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #add(double)
     */
    @Deprecated
    default boolean add(Double e) {
        this.addLast(e != null ? e.doubleValue() : (double) 0);
        return true;
    }

    void addFirst(double e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addFirst(double)
     */
    @Deprecated
    default void addFirst(Double e) {
        this.addFirst(e != null ? e.doubleValue() : (double) 0);
    }

    void addLast(double e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addLast(double)
     */
    @Deprecated
    default void addLast(Double e) {
        this.addLast(e != null ? e.doubleValue() : (double) 0);
    }

    default boolean offer(double e) {
        this.addLast(e);
        return true;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #offer(double)
     */
    @Deprecated
    default boolean offer(Double e) {
        this.addLast(e != null ? e.doubleValue() : (double) 0);
        return true;
    }

    default boolean offerFirstRaw(double e) {
        this.addFirst(e);
        return true;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #offerFirstRaw(double)
     */
    @Deprecated
    default boolean offerFirst(Double e) {
        this.addFirst(e);
        return true;
    }

    default boolean offerLastRaw(double e) {
        this.addLast(e);
        return true;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #offerLastRaw(double)
     */
    @Deprecated
    default boolean offerLast(Double e) {
        this.addLast(e);
        return true;
    }

    default double pollRaw() {
        return this.pollFirstRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollRaw()
     */
    @Deprecated
    default Double poll() {
        return this.pollFirstRaw();
    }

    double pollFirstRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirstRaw()
     */
    @Deprecated
    default Double pollFirst() {
        return this.pollFirstRaw();
    }

    double pollLastRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLastRaw()
     */
    @Deprecated
    default Double pollLast() {
        return this.pollLastRaw();
    }

    default double removeRaw() {
        return this.removeFirstRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeRaw()
     */
    @Deprecated
    default Double remove() {
        return this.removeFirstRaw();
    }

    default double removeFirstRaw() {
        final double result;
        if ((result = this.pollFirstRaw()) == this.getNullElementRaw()) throw new NoSuchElementException();
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeFirstRaw()
     */
    @Deprecated
    default Double removeFirst() {
        return this.removeFirstRaw();
    }

    default double removeLastRaw() {
        final double result;
        if ((result = this.pollLastRaw()) == this.getNullElementRaw()) throw new NoSuchElementException();
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeLastRaw()
     */
    @Deprecated
    default Double removeLast() {
        return this.removeLastRaw();
    }

    /**
     * @deprecated Please use the corresponding method that renamed ones to
     * avoid overload-resolution ambiguity.
     * @see #removeElement(double)
     */
    @Deprecated
    default boolean remove(Object o) {
        return DoubleQueue.super.remove(o);
    }

    boolean removeFirstOccurrence(double o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeFirstOccurrence(double)
     */
    @Deprecated
    default boolean removeFirstOccurrence(Object o) {
        if (o != null && !(o instanceof Double)) return false;
        return this.removeFirstOccurrence(o != null ? ((Double) o).doubleValue() : (double) 0);
    }

    boolean removeLastOccurrence(double o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeLastOccurrence(double)
     */
    @Deprecated
    default boolean removeLastOccurrence(Object o) {
        if (o != null && !(o instanceof Double)) return false;
        return this.removeLastOccurrence(o != null ? ((Double) o).doubleValue() : (double) 0);
    }

    default void push(double e) {
        this.addFirst(e);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #push(double)
     */
    @Deprecated
    default void push(Double e) {
        this.addFirst(e != null ? e.doubleValue() : (double) 0);
    }

    default double popRaw() {
        return this.removeFirstRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #popRaw()
     */
    @Deprecated
    default Double pop() {
        return this.popRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #contains(double)
     */
    @Deprecated
    default boolean contains(Object o) {
        return DoubleQueue.super.contains(o);
    }

    boolean tryPollExecuteOfferReversed(@NotNull DoubleConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #tryPollExecuteOfferReversed(DoubleConsumer)
     */
    @Deprecated
    default boolean tryPollExecuteOfferReversed(@NotNull Consumer<? super Double> action) {
        return this.tryPollExecuteOfferReversed((double l_t) -> action.accept(l_t));
    }

    @NotNull DoubleIterator descendingIterator();
}
