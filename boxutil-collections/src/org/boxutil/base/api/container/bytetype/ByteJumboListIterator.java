package org.boxutil.base.api.container.bytetype;

import org.boxutil.base.api.container.JumboListIterator;

public interface ByteJumboListIterator extends ByteListIterator, JumboListIterator<Byte> {
    long nextIndex64();

    long previousIndex64();
}
