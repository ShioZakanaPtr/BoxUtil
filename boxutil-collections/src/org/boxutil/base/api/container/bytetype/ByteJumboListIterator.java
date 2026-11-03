package org.boxutil.base.api.container.bytetype;

import org.boxutil.base.api.container.JumboListIterator;
import org.boxutil.base.api.container.longtype.LongListIterator;

public interface ByteJumboListIterator extends ByteListIterator, JumboListIterator<Byte> {
    long nextIndex64();

    long previousIndex64();
}
