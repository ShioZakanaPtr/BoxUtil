package io.github.shiozakanaptr.acext.api.collection.chartype;

import io.github.shiozakanaptr.acext.api.collection.JumboListIterator;

/**
 * Type specialization interface.
 *
 * @see JumboListIterator
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface CharJumboListIterator extends CharListIterator, JumboListIterator<Character> {
    long nextIndex64();

    long previousIndex64();
}
