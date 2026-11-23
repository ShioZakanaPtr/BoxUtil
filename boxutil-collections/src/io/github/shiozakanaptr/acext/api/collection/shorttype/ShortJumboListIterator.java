package io.github.shiozakanaptr.acext.api.collection.shorttype;

import io.github.shiozakanaptr.acext.api.collection.JumboListIterator;

/**
 * Type specialization interface.
 *
 * @see JumboListIterator
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface ShortJumboListIterator extends ShortListIterator, JumboListIterator<Short> {
    long nextIndex64();

    long previousIndex64();
}
