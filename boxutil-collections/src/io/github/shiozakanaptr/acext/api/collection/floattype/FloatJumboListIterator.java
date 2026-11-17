package io.github.shiozakanaptr.acext.api.collection.floattype;

import io.github.shiozakanaptr.acext.api.collection.JumboListIterator;

public interface FloatJumboListIterator extends FloatListIterator, JumboListIterator<Float> {
    long nextIndex64();

    long previousIndex64();
}
