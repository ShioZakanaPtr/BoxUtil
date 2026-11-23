package io.github.shiozakanaptr.acext.util.collection.shorttype;

import io.github.shiozakanaptr.acext.api.collection.shorttype.*;

public abstract class AbstractShortList extends AbstractShortCollection implements ShortList {
    protected AbstractShortList() {}

    public void shrink() {
        this.resize(0);
    }

    public abstract short getRaw(int index);

    public short set(int index, short element) {
        throw new UnsupportedOperationException();
    }

    public boolean add(short e) {
        this.add(this.size(), e);
        return true;
    }

    public void add(int index, short element) {
        throw new UnsupportedOperationException();
    }

    public short removeAtRaw(int index) {
        throw new UnsupportedOperationException();
    }

    public boolean removeElement(short o) {
        ShortBidiIterator it = this.iterator();
        while (it.hasNext()) {
            if (o == it.nextShort()) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    public int indexOf(short o) {
        ShortListIterator it = this.listIterator();
        while (it.hasNext()) {
            if (o == it.nextShort()) {
                return it.previousIndex();
            }
        }
        return -1;
    }

    public int lastIndexOf(short o) {
        ShortListIterator it = this.listIterator(this.size());
        while (it.hasPrevious()) {
            if (o == it.previousShort()) {
                return it.nextIndex();
            }
        }
        return -1;
    }

    public abstract ShortBidiIterator iterator();

    public abstract ShortBidiIterator iteratorReversed();

    public abstract ShortSpliterator spliterator();
}
