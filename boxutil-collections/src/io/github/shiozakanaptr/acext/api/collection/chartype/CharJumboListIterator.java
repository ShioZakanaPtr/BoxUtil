package io.github.shiozakanaptr.acext.api.collection.chartype;

import io.github.shiozakanaptr.acext.api.collection.JumboListIterator;

public interface CharJumboListIterator extends CharListIterator, JumboListIterator<Character> {
    long nextIndex64();

    long previousIndex64();
}
