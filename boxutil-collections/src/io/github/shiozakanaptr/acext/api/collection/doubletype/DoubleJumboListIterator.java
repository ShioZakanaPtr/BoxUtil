package io.github.shiozakanaptr.acext.api.collection.doubletype;

import io.github.shiozakanaptr.acext.api.collection.JumboListIterator;

public interface DoubleJumboListIterator extends DoubleListIterator, JumboListIterator<Double> {
    long nextIndex64();

    long previousIndex64();
}
