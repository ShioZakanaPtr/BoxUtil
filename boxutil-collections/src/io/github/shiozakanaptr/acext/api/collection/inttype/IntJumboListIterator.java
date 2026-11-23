package io.github.shiozakanaptr.acext.api.collection.inttype;

import io.github.shiozakanaptr.acext.api.collection.JumboListIterator;

/**
 * Type specialization interface.
 *
 * @see JumboListIterator
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface IntJumboListIterator extends IntListIterator, JumboListIterator<Integer> {
    long nextIndex64();

    long previousIndex64();
}
