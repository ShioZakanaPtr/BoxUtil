package io.github.shiozakanaptr.acext.api.collection.floattype;

import io.github.shiozakanaptr.acext.api.collection.DequeExt;
import io.github.shiozakanaptr.acext.util.function.floattype.FloatConsumer;
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
public interface FloatDeque extends DequeExt<Float>, FloatQueue {
    default float peekRaw() {
        return this.peekFirstRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #peekRaw()
     */
    @Deprecated
    default Float peek() {
        return this.peekFirstRaw();
    }

    float peekFirstRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #peekFirstRaw()
     */
    @Deprecated
    default Float peekFirst() {
        return this.peekFirstRaw();
    }

    float peekLastRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #peekLastRaw()
     */
    @Deprecated
    default Float peekLast() {
        return this.peekLastRaw();
    }

    default float elementRaw() {
        return this.getFirstRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #elementRaw()
     */
    @Deprecated
    default Float element() {
        return this.getFirstRaw();
    }

    default float getFirstRaw() {
        float result;
        if ((result = this.peekFirstRaw()) == this.getNullElementRaw()) throw new NoSuchElementException();
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getFirstRaw()
     */
    @Deprecated
    default Float getFirst() {
        return this.getFirstRaw();
    }

    default float getLastRaw() {
        float result;
        if ((result = this.peekLastRaw()) == this.getNullElementRaw()) throw new NoSuchElementException();
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getLastRaw()
     */
    @Deprecated
    default Float getLast() {
        return this.getLastRaw();
    }

    default boolean add(float e) {
        this.addLast(e);
        return true;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #add(float)
     */
    @Deprecated
    default boolean add(Float e) {
        this.addLast(e != null ? e.floatValue() : (float) 0);
        return true;
    }

    void addFirst(float e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addFirst(float)
     */
    @Deprecated
    default void addFirst(Float e) {
        this.addFirst(e != null ? e.floatValue() : (float) 0);
    }

    void addLast(float e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addLast(float)
     */
    @Deprecated
    default void addLast(Float e) {
        this.addLast(e != null ? e.floatValue() : (float) 0);
    }

    default boolean offer(float e) {
        this.addLast(e);
        return true;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #offer(float)
     */
    @Deprecated
    default boolean offer(Float e) {
        this.addLast(e != null ? e.floatValue() : (float) 0);
        return true;
    }

    default boolean offerFirstRaw(float e) {
        this.addFirst(e);
        return true;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #offerFirstRaw(float)
     */
    @Deprecated
    default boolean offerFirst(Float e) {
        this.addFirst(e);
        return true;
    }

    default boolean offerLastRaw(float e) {
        this.addLast(e);
        return true;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #offerLastRaw(float)
     */
    @Deprecated
    default boolean offerLast(Float e) {
        this.addLast(e);
        return true;
    }

    default float pollRaw() {
        return this.pollFirstRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollRaw()
     */
    @Deprecated
    default Float poll() {
        return this.pollFirstRaw();
    }

    float pollFirstRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirstRaw()
     */
    @Deprecated
    default Float pollFirst() {
        return this.pollFirstRaw();
    }

    float pollLastRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLastRaw()
     */
    @Deprecated
    default Float pollLast() {
        return this.pollLastRaw();
    }

    default float removeRaw() {
        return this.removeFirstRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeRaw()
     */
    @Deprecated
    default Float remove() {
        return this.removeFirstRaw();
    }

    default float removeFirstRaw() {
        final float result;
        if ((result = this.pollFirstRaw()) == this.getNullElementRaw()) throw new NoSuchElementException();
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeFirstRaw()
     */
    @Deprecated
    default Float removeFirst() {
        return this.removeFirstRaw();
    }

    default float removeLastRaw() {
        final float result;
        if ((result = this.pollLastRaw()) == this.getNullElementRaw()) throw new NoSuchElementException();
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeLastRaw()
     */
    @Deprecated
    default Float removeLast() {
        return this.removeLastRaw();
    }

    boolean removeFirstOccurrence(float o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeFirstOccurrence(float)
     */
    @Deprecated
    default boolean removeFirstOccurrence(Object o) {
        if (o != null && !(o instanceof Float)) return false;
        return this.removeFirstOccurrence(o != null ? ((Float) o).floatValue() : (float) 0);
    }

    boolean removeLastOccurrence(float o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeLastOccurrence(float)
     */
    @Deprecated
    default boolean removeLastOccurrence(Object o) {
        if (o != null && !(o instanceof Float)) return false;
        return this.removeLastOccurrence(o != null ? ((Float) o).floatValue() : (float) 0);
    }

    default void push(float e) {
        this.addFirst(e);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #push(float)
     */
    @Deprecated
    default void push(Float e) {
        this.addFirst(e != null ? e.floatValue() : (float) 0);
    }

    default float popRaw() {
        return this.removeFirstRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #popRaw()
     */
    @Deprecated
    default Float pop() {
        return this.popRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #contains(float)
     */
    @Deprecated
    default boolean contains(Object o) {
        return FloatQueue.super.contains(o);
    }

    boolean tryPollExecuteOfferReversed(@NotNull FloatConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #tryPollExecuteOfferReversed(FloatConsumer)
     */
    @Deprecated
    default boolean tryPollExecuteOfferReversed(@NotNull Consumer<? super Float> action) {
        return this.tryPollExecuteOfferReversed((float l_t) -> action.accept(l_t));
    }

    @NotNull
    FloatBidiIterator iterator();

    @NotNull
    FloatBidiIterator iteratorReversed();

    @NotNull FloatIterator descendingIterator();
}
