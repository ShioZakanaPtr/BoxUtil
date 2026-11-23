package io.github.shiozakanaptr.acext.util.collection.longtype;

import io.github.shiozakanaptr.acext.api.collection.longtype.*;

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
        LongBidiIterator it = this.iterator();
        while (it.hasNext()) {
            if (o == it.nextLong()) {
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

    public abstract LongBidiIterator iterator();

    public abstract LongBidiIterator iteratorReversed();

    public abstract LongSpliterator spliterator();
}
