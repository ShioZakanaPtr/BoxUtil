package io.github.shiozakanaptr.acext.api.collection.chartype;

import java.util.ListIterator;

public interface CharListIterator extends ListIterator<Character>, CharIterator {
    boolean hasNext();

    int nextIndex();

    char nextChar();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #nextChar()
     */
    @Deprecated
    default Character next() {
        return this.nextChar();
    }

    boolean hasPrevious();

    int previousIndex();

    char previousChar();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #previousChar()
     */
    @Deprecated
    default Character previous() {
        return this.previousChar();
    }

    void remove();

    void set(char e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #set(char)
     */
    @Deprecated
    default void set(Character e) {
        this.set(e != null ? e.charValue() : (char) 0);
    }

    void add(char e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #add(char)
     */
    @Deprecated
    default void add(Character e) {
        this.add(e != null ? e.charValue() : (char) 0);
    }
}
