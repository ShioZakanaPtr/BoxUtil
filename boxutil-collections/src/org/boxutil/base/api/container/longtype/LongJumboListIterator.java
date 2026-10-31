package org.boxutil.base.api.container.longtype;

import org.boxutil.base.api.container.JumboListIterator;

public interface LongJumboListIterator extends LongListIterator, JumboListIterator<Long> {
    long nextIndex64();

    long previousIndex64();
}
