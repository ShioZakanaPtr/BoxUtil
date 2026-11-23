package io.github.shiozakanaptr.acext.util.collection.doubletype;

import io.github.shiozakanaptr.acext.api.collection.doubletype.*;

public abstract class AbstractDoubleList extends AbstractDoubleCollection implements DoubleList {
    protected AbstractDoubleList() {}

    public void shrink() {
        this.resize(0);
    }

    public abstract double getRaw(int index);

    public double set(int index, double element) {
        throw new UnsupportedOperationException();
    }

    public boolean add(double e) {
        this.add(this.size(), e);
        return true;
    }

    public void add(int index, double element) {
        throw new UnsupportedOperationException();
    }

    public double removeAtRaw(int index) {
        throw new UnsupportedOperationException();
    }

    public boolean removeElement(double o) {
        DoubleBidiIterator it = this.iterator();
        while (it.hasNext()) {
            if (o == it.nextDouble()) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    public int indexOf(double o) {
        DoubleListIterator it = this.listIterator();
        while (it.hasNext()) {
            if (o == it.nextDouble()) {
                return it.previousIndex();
            }
        }
        return -1;
    }

    public int lastIndexOf(double o) {
        DoubleListIterator it = this.listIterator(this.size());
        while (it.hasPrevious()) {
            if (o == it.previousDouble()) {
                return it.nextIndex();
            }
        }
        return -1;
    }

    public abstract DoubleBidiIterator iterator();

    public abstract DoubleBidiIterator iteratorReversed();

    public abstract DoubleSpliterator spliterator();
}
