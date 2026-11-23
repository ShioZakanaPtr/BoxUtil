package io.github.shiozakanaptr.acext.api.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.JumboListIterator;

/**
 * Type specialization interface.
 *
 * @see JumboListIterator
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface ByteJumboListIterator extends ByteListIterator, JumboListIterator<Byte> {
    long nextIndex64();

    long previousIndex64();
}
