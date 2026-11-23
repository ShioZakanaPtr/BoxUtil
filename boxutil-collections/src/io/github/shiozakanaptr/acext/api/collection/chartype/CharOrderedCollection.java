package io.github.shiozakanaptr.acext.api.collection.chartype;

import io.github.shiozakanaptr.acext.api.collection.OrderedCollection;
import org.jetbrains.annotations.NotNull;

/**
 * Type specialization interface.
 *
 * @see OrderedCollection
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface CharOrderedCollection extends CharCollection, OrderedCollection<Character> {
    char getFirstRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getFirstRaw()
     */
    @Deprecated
    default Character getFirst() {
        return this.getFirstRaw();
    }

    char getLastRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getLastRaw()
     */
    @Deprecated
    default Character getLast() {
        return this.getLastRaw();
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

    char removeFirstRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeFirstRaw()
     */
    @Deprecated
    default Character removeFirst() {
        return this.removeFirstRaw();
    }

    char removeLastRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeLastRaw()
     */
    @Deprecated
    default Character removeLast() {
        return this.removeLastRaw();
    }

    @NotNull CharBidiIterator iterator();

    @NotNull CharBidiIterator iteratorReversed();
}
