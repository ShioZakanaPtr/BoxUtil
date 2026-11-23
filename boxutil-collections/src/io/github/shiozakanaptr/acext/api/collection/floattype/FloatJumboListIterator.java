package io.github.shiozakanaptr.acext.api.collection.floattype;

import io.github.shiozakanaptr.acext.api.collection.JumboListIterator;

/**
 * Type specialization interface.
 *
 * @see JumboListIterator
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface FloatJumboListIterator extends FloatListIterator, JumboListIterator<Float> {
    long nextIndex64();

    long previousIndex64();
}
