package io.github.shiozakanaptr.acext.api.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.JumboListIterator;

public interface ByteJumboListIterator extends ByteListIterator, JumboListIterator<Byte> {
    long nextIndex64();

    long previousIndex64();
}
