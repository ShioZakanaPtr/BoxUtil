package io.github.shiozakanaptr.acext.api.collection.inttype;

import io.github.shiozakanaptr.acext.api.collection.JumboListIterator;

public interface IntJumboListIterator extends IntListIterator, JumboListIterator<Integer> {
    long nextIndex64();

    long previousIndex64();
}
