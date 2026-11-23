package io.github.shiozakanaptr.acext.api.collection.longtype;

import io.github.shiozakanaptr.acext.api.collection.JumboListIterator;

/**
 * Type specialization interface.
 *
 * @see JumboListIterator
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface LongJumboListIterator extends LongListIterator, JumboListIterator<Long> {
    long nextIndex64();

    long previousIndex64();
}
