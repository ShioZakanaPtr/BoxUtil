package io.github.shiozakanaptr.acext.api.collection.chartype;

import io.github.shiozakanaptr.acext.api.collection.DequeExt;
import io.github.shiozakanaptr.acext.util.function.chartype.CharConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.function.Consumer;

public interface CharDeque extends DequeExt<Character>, CharQueue {
    default char peekRaw() {
        return this.peekFirstRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #peekRaw()
     */
    @Deprecated
    default Character peek() {
        return this.peekFirstRaw();
    }

    char peekFirstRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #peekFirstRaw()
     */
    @Deprecated
    default Character peekFirst() {
        return this.peekFirstRaw();
    }

    char peekLastRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #peekLastRaw()
     */
    @Deprecated
    default Character peekLast() {
        return this.peekLastRaw();
    }

    default char elementRaw() {
        return this.getFirstRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #elementRaw()
     */
    @Deprecated
    default Character element() {
        return this.getFirstRaw();
    }

    default char getFirstRaw() {
        char result;
        if ((result = this.peekFirstRaw()) == this.getNullElementRaw()) throw new NoSuchElementException();
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getFirstRaw()
     */
    @Deprecated
    default Character getFirst() {
        return this.getFirstRaw();
    }

    default char getLastRaw() {
        char result;
        if ((result = this.peekLastRaw()) == this.getNullElementRaw()) throw new NoSuchElementException();
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getLastRaw()
     */
    @Deprecated
    default Character getLast() {
        return this.getLastRaw();
    }

    default boolean add(char e) {
        this.addLast(e);
        return true;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #add(char)
     */
    @Deprecated
    default boolean add(Character e) {
        this.addLast(e != null ? e.charValue() : (char) 0);
        return true;
    }

    void addFirst(char e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addFirst(char)
     */
    @Deprecated
    default void addFirst(Character e) {
        this.addFirst(e != null ? e.charValue() : (char) 0);
    }

    void addLast(char e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addLast(char)
     */
    @Deprecated
    default void addLast(Character e) {
        this.addLast(e != null ? e.charValue() : (char) 0);
    }

    default boolean offer(char e) {
        this.addLast(e);
        return true;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #offer(char)
     */
    @Deprecated
    default boolean offer(Character e) {
        this.addLast(e != null ? e.charValue() : (char) 0);
        return true;
    }

    default boolean offerFirstRaw(char e) {
        this.addFirst(e);
        return true;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #offerFirstRaw(char)
     */
    @Deprecated
    default boolean offerFirst(Character e) {
        this.addFirst(e);
        return true;
    }

    default boolean offerLastRaw(char e) {
        this.addLast(e);
        return true;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #offerLastRaw(char)
     */
    @Deprecated
    default boolean offerLast(Character e) {
        this.addLast(e);
        return true;
    }

    default char pollRaw() {
        return this.pollFirstRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollRaw()
     */
    @Deprecated
    default Character poll() {
        return this.pollFirstRaw();
    }

    char pollFirstRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollFirstRaw()
     */
    @Deprecated
    default Character pollFirst() {
        return this.pollFirstRaw();
    }

    char pollLastRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollLastRaw()
     */
    @Deprecated
    default Character pollLast() {
        return this.pollLastRaw();
    }

    default char removeRaw() {
        return this.removeFirstRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeRaw()
     */
    @Deprecated
    default Character remove() {
        return this.removeFirstRaw();
    }

    default char removeFirstRaw() {
        final char result;
        if ((result = this.pollFirstRaw()) == this.getNullElementRaw()) throw new NoSuchElementException();
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeFirstRaw()
     */
    @Deprecated
    default Character removeFirst() {
        return this.removeFirstRaw();
    }

    default char removeLastRaw() {
        final char result;
        if ((result = this.pollLastRaw()) == this.getNullElementRaw()) throw new NoSuchElementException();
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeLastRaw()
     */
    @Deprecated
    default Character removeLast() {
        return this.removeLastRaw();
    }

    /**
     * @deprecated Please use the corresponding method that renamed ones to
     * avoid overload-resolution ambiguity.
     * @see #removeElement(char)
     */
    @Deprecated
    default boolean remove(Object o) {
        return CharQueue.super.remove(o);
    }

    boolean removeFirstOccurrence(char o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeFirstOccurrence(char)
     */
    @Deprecated
    default boolean removeFirstOccurrence(Object o) {
        if (o != null && !(o instanceof Character)) return false;
        return this.removeFirstOccurrence(o != null ? ((Character) o).charValue() : (char) 0);
    }

    boolean removeLastOccurrence(char o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeLastOccurrence(char)
     */
    @Deprecated
    default boolean removeLastOccurrence(Object o) {
        if (o != null && !(o instanceof Character)) return false;
        return this.removeLastOccurrence(o != null ? ((Character) o).charValue() : (char) 0);
    }

    default void push(char e) {
        this.addFirst(e);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #push(char)
     */
    @Deprecated
    default void push(Character e) {
        this.addFirst(e != null ? e.charValue() : (char) 0);
    }

    default char popRaw() {
        return this.removeFirstRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #popRaw()
     */
    @Deprecated
    default Character pop() {
        return this.popRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #contains(char)
     */
    @Deprecated
    default boolean contains(Object o) {
        return CharQueue.super.contains(o);
    }

    boolean tryPollExecuteOfferReversed(@NotNull CharConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #tryPollExecuteOfferReversed(CharConsumer)
     */
    @Deprecated
    default boolean tryPollExecuteOfferReversed(@NotNull Consumer<? super Character> action) {
        return this.tryPollExecuteOfferReversed((char l_t) -> action.accept(l_t));
    }

    @NotNull CharIterator descendingIterator();
}
