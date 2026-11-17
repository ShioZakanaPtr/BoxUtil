package io.github.shiozakanaptr.acext.api.collection.shorttype;

import io.github.shiozakanaptr.acext.api.collection.JumboListIterator;

public interface ShortJumboListIterator extends ShortListIterator, JumboListIterator<Short> {
    long nextIndex64();

    long previousIndex64();
}
