package io.github.shiozakanaptr.acext.util.collection.inttype;

import io.github.shiozakanaptr.acext.api.collection.inttype.*;

public abstract class AbstractIntList extends AbstractIntCollection implements IntList {
    protected AbstractIntList() {}

    public void shrink() {
        this.resize(0);
    }

    public abstract int getRaw(int index);

    public int set(int index, int element) {
        throw new UnsupportedOperationException();
    }

    public boolean add(int e) {
        this.add(this.size(), e);
        return true;
    }

    public void add(int index, int element) {
        throw new UnsupportedOperationException();
    }

    public int removeAtRaw(int index) {
        throw new UnsupportedOperationException();
    }

    public boolean removeElement(int o) {
        IntBidiIterator it = this.iterator();
        while (it.hasNext()) {
            if (o == it.nextInt()) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    public int indexOf(int o) {
        IntListIterator it = this.listIterator();
        while (it.hasNext()) {
            if (o == it.nextInt()) {
                return it.previousIndex();
            }
        }
        return -1;
    }

    public int lastIndexOf(int o) {
        IntListIterator it = this.listIterator(this.size());
        while (it.hasPrevious()) {
            if (o == it.previousInt()) {
                return it.nextIndex();
            }
        }
        return -1;
    }

    public abstract IntBidiIterator iterator();

    public abstract IntBidiIterator iteratorReversed();

    public abstract IntSpliterator spliterator();
}
