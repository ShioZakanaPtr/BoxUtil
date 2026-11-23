package io.github.shiozakanaptr.acext.api.collection.doubletype;

import io.github.shiozakanaptr.acext.api.collection.JumboListIterator;

/**
 * Type specialization interface.
 *
 * @see JumboListIterator
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface DoubleJumboListIterator extends DoubleListIterator, JumboListIterator<Double> {
    long nextIndex64();

    long previousIndex64();
}
