package org.boxutil.util.collection.longtype;

import org.boxutil.base.api.container.longtype.LongIterator;
import org.boxutil.base.api.container.longtype.LongList;
import org.boxutil.base.api.container.longtype.LongListIterator;
import org.boxutil.base.api.container.longtype.LongSpliterator;

public abstract class AbstractLongList extends AbstractLongCollection implements LongList {
    protected AbstractLongList() {}

    public void shrink() {
        this.resize(0);
    }

    public abstract long getRaw(int index);

    public long set(int index, long element) {
        throw new UnsupportedOperationException();
    }

    public boolean add(long e) {
        this.add(this.size(), e);
        return true;
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

    public boolean remove(long o) {
        return this.removeElement(o);
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

    public abstract LongIterator iterator();

    public abstract LongSpliterator spliterator();
}
