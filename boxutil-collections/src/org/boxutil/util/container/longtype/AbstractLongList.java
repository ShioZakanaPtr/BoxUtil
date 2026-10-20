package org.boxutil.util.container.longtype;

import org.boxutil.base.api.container.longtype.LongIterator;
import org.boxutil.base.api.container.longtype.LongList;
import org.boxutil.base.api.container.longtype.LongListIterator;
import org.boxutil.base.api.container.longtype.LongSpliterator;
import org.jetbrains.annotations.NotNull;

import java.util.ListIterator;
import java.util.Objects;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;
import java.util.function.LongUnaryOperator;

public abstract class AbstractLongList extends AbstractLongCollection implements LongList {
    protected AbstractLongList() {}

    public abstract long getRaw(int index);

    public long set(int index, long element) {
        throw new UnsupportedOperationException();
    }

    public void add(int index, long element) {
        throw new UnsupportedOperationException();
    }

    public long removeAtRaw(int index) {
        throw new UnsupportedOperationException();
    }

    public boolean removeElement(long o) {
        final LongIterator it = this.iterator();
        while (it.hasNext()) {
            if (it.nextLong() == o) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    public int indexOf(long o) {
        LongListIterator it = this.listIterator();
        while (it.hasNext()) {
            if (o == it.nextLong()) {
                return it.previousIndex();
            }
        }
        return -1;
    }

    public int lastIndexOf(long o) {
        LongListIterator it = this.listIterator(this.size());
        while (it.hasPrevious()) {
            if (o == it.previousLong()) {
                return it.nextIndex();
            }
        }
        return -1;
    }
}
