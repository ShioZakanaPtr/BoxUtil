package io.github.shiozakanaptr.acext.api.collection.longtype;

import io.github.shiozakanaptr.acext.api.collection.JumboListIterator;

public interface LongJumboListIterator extends LongListIterator, JumboListIterator<Long> {
    long nextIndex64();

    long previousIndex64();
}
