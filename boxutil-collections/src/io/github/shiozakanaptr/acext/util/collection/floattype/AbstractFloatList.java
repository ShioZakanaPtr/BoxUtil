package io.github.shiozakanaptr.acext.util.collection.floattype;

import io.github.shiozakanaptr.acext.api.collection.floattype.*;

public abstract class AbstractFloatList extends AbstractFloatCollection implements FloatList {
    protected AbstractFloatList() {}

    public void shrink() {
        this.resize(0);
    }

    public abstract float getRaw(int index);

    public float set(int index, float element) {
        throw new UnsupportedOperationException();
    }

    public boolean add(float e) {
        this.add(this.size(), e);
        return true;
    }

    public void add(int index, float element) {
        throw new UnsupportedOperationException();
    }

    public float removeAtRaw(int index) {
        throw new UnsupportedOperationException();
    }

    public boolean removeElement(float o) {
        FloatBidiIterator it = this.iterator();
        while (it.hasNext()) {
            if (o == it.nextFloat()) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    public int indexOf(float o) {
        FloatListIterator it = this.listIterator();
        while (it.hasNext()) {
            if (o == it.nextFloat()) {
                return it.previousIndex();
            }
        }
        return -1;
    }

    public int lastIndexOf(float o) {
        FloatListIterator it = this.listIterator(this.size());
        while (it.hasPrevious()) {
            if (o == it.previousFloat()) {
                return it.nextIndex();
            }
        }
        return -1;
    }

    public abstract FloatBidiIterator iterator();

    public abstract FloatBidiIterator iteratorReversed();

    public abstract FloatSpliterator spliterator();
}
